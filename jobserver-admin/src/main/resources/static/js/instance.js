var Instance = function () {
    let winWidth, winHeight;
    let table = layui.table;
    let form = layui.form;
    let dropdown = layui.dropdown;
    let clusterMap = new Map();

    let nodeCodeEditor, nodeLogEditor;
    let jobTextEditor, jobConfigEditor;

    return {
        init: function () {
            winWidth = $(window).width() * 0.9;
            winHeight = $(window).height() * 0.9;

            $("#clusterVals option").each(function() {
                clusterMap.set($(this).val(), $(this).text())
            });

            let cols = [
                [{
                    type: 'checkbox',
                    fixed: 'left'
                },
                    {
                        title: '集群',
                        field: 'clusterCode',
                        align: 'center',
                        width: 90,
                        templet: function(record) {
                            const clusterCode = record.clusterCode;
                            return clusterMap.get(clusterCode)
                        }
                    },
                    {
                        title: '实例Code',
                        field: 'code',
                        align: 'center'
                    },
                    {
                        title: '实例名称',
                        field: 'name',
                        align: 'center',
                        width: 150
                    },
                    {
                        title: '作业类型',
                        field: 'jobType',
                        align: 'center',
                        width: 90
                    },
                    {
                        title: '实例类型',
                        field: 'instanceType',
                        align: 'center',
                        width: 90
                    },
                    {
                        title: '客户端',
                        field: 'clientName',
                        align: 'center',
                        width: 90
                    },
                    {
                        title: '负责人',
                        field: 'owner',
                        align: 'center',
                        width: 70
                    },
                    {
                        title: '耗时',
                        field: 'runTimes',
                        align: 'center',
                        width: 80,
                        templet: function(record) {
                            const times = parseInt(record.runTimes);
                            const hours   = Math.floor(times / 3600000);
                            const minutes = Math.floor((times - (hours * 3600000)) / 60000);
                            const seconds = Math.floor((times - (hours * 3600000) - (minutes * 60000))/1000);
                            const millisecond = times - (hours * 3600000) - (minutes * 60000) - (seconds * 1000);

                            let time = "";
                            if (hours   > 0) {time += hours + "小时";}
                            if (minutes > 0) {time += minutes + "分";}
                            if (seconds > 0) {time += seconds + "秒";}

                            if (time === "") time = "1秒";
                            return time;
                        }
                    },
                    {
                        title: '调度时间',
                        field: 'scheduleTime',
                        align: 'right'
                    },
                    {
                        title: '开始时间',
                        field: 'startTime',
                        align: 'right'
                    },
                    {
                        title: '结束时间',
                        field: 'endTime',
                        align: 'right'
                    },
                    {
                        title: '状态',
                        field: 'status',
                        align: 'center',
                        width: 80,
                        templet: function(record) {
                            const status = record.status;
                            if (status === "FINISHED") {
                                return '<span style="font-weight:bold;color: #5FB878">FINISHED</span>'
                            } else if (status === "FAILED" || status === "KILLED") {
                                return '<span style="font-weight:bold;color: #FF5722">' + status + '</span>'
                            } else if (status === "RUNNING") {
                                return '<span style="font-weight:bold;color: #FFB800">' + status + '</span>'
                            } else {
                                return '<span style="font-weight:bold">' + status + '</span>'
                            }
                        }
                    },
                    {
                        title: '操作',
                        toolbar: '#instance-bar',
                        align: 'right',
                        fixed: 'right',
                        width: 50
                    }
                ]
            ]

            table.render({
                elem: '#instance-table',
                url: '/instance/queryInstances',
                page: true,
                cols: cols,
                skin: 'line',
                parseData: function (res) {
                    return {
                        "code": 0,
                        "count": res.total,
                        "data": res.rows
                    };
                },
                toolbar: '#toolbarDemo',
                defaultToolbar:[],
                done: function(res, curr, count) {
                    for (var i = 0; i < res.data.length; i++) {
                        const row = res.data[i];
                        const menus = []
                        menus.push({title: '查看日志', id: "viewLog", code: row.code, scheduleTime: row.scheduleTime});
                        menus.push({title: '修改作业', id: "editJob", code: row.code, instanceId: row.id});
                        menus.push({title: '查看代码', id: "viewCode", code: row.code});
                        menus.push({title: '下载日志', id: "downloadLog", code: row.code, scheduleTime: row.scheduleTime});
                        if (row.status === "FINISHED" || row.status === "FAILED"
                            || row.status === "KILLED" || row.status === 'SUBMITTED' || row.status === 'LOCKED') {
                            menus.push({title: '重新运行', id: "restart", code: row.code});
                        } else if (row.status === "RUNNING" || row.status === "WAITING") {
                            menus.push({title: '终止运行', id: "killJob", code: row.code});
                        }
                        menus.push({title: '删除', id: "deleteInstance", code: row.code});

                        dropdown.render({
                            elem: '#opt_' + row.code,
                            data: menus,
                            id: "#opt_" + row.code,
                            click: function(obj) {
                                if (obj.id === "viewLog") {
                                    Instance.viewLog(obj.code, obj.scheduleTime, false)
                                } else if (obj.id === "viewCode") {
                                    Instance.viewCode(obj.code)
                                } else if (obj.id === "editJob") {
                                    Instance.newInstanceWin(obj.code, obj.instanceId)
                                } else if (obj.id === "downloadLog") {
                                    Instance.downloadLog(obj.code, obj.scheduleTime)
                                } else if (obj.id === "restart") {
                                    Instance.restart(obj.code, obj.scheduleTime)
                                } else if (obj.id === "killJob") {
                                    Instance.killJob(obj.code, obj.scheduleTime)
                                } else if (obj.id === "deleteInstance") {
                                    Instance.deleteInstance(obj.code)
                                }
                            }
                        });
                    }
                }
            });

            table.on('toolbar(instance-table)', function(obj) {
                if (obj.event === 'refresh') {
                    Instance.refresh();
                }
            });

            form.on('submit(user-query)', function(data) {
                table.reload('instance-table', {
                    where: data.field
                })
                Instance.queryInstanceStatistics();
                return false;
            });

            form.on('select(jobType)', function(data){
                const jobType = data.value;
                if ("SPARK_PYTHON" === jobType) {
                    Instance.setEditorValue(jobTextEditor, "def main(sparkSession):\n    ")
                }
            });

            jobTextEditor = Instance.getEditor(jobTextEditor, "jobTextEditor", "ace/mode/sql");
            jobConfigEditor = Instance.getEditor(jobConfigEditor, "jobConfigEditor", "ace/mode/properties");
            Instance.queryInstanceStatistics();
        },

        getEditor: function(editor, editorId, mode) {
            editor = ace.edit(editorId);
            editor.setTheme("ace/theme/cobalt");
            editor.getSession().setMode(mode);
            $('#' + editorId).height((winHeight - 250) + "px");
            editor.resize();
            return editor;
        },

        setEditorValue : function(editor, config) {
            if (config == null) {
                editor.setValue("");
            } else {
                editor.setValue(config);
            }
            editor.clearSelection();
        },

        restart : function (instanceCode) {
            layer.confirm('确认重跑当前实例?', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    async: true,
                    type: "POST",
                    url: '/instance/restart?instanceCode=' + instanceCode,
                    success: function (data) {
                        if (data.success) {
                            layer.msg('操作成功', {icon: 1});
                            Instance.refresh()
                        }
                    },
                });
            });
        },

        batchDownloadLogs : function () {
            let rst = table.checkStatus('instance-table')
            if (rst.data.length === 0) {
                toastr.error({message: "请选择下载作业"});
            } else {
                let codes = []
                rst.data.map((item)=>{
                    codes.push(item.code);
                })

                let url = "/instance/batchDownloadLogs?codes=" + codes.join(',');
                window.open(url, '_blank');
            }
        },

        newInstanceWin : function(code, instanceId) {
            let btns = ['保存']
            if (instanceId) {
                $.ajax({
                    async: true,
                    type : "GET",
                    url: '/instance/queryInstance',
                    data: { instanceId: instanceId },
                    success: function (result) {
                        if (result.success) {
                            let data = result.data;
                            form.val('newInstanceForm', data);
                        }
                    }
                })

                $.ajax({
                    async: true,
                    type : "GET",
                    url: '/instance/queryInstanceContent',
                    data: { code: code },
                    success: function (result) {
                        if (result.success) {
                            let data = result.data;
                            Instance.setEditorValue(jobConfigEditor, data.jobConfig)
                            Instance.setEditorValue(jobTextEditor, data.jobText)
                        }
                    }
                })

                btns.push("保存并运行")
            }

            var index = layer.open({
                type: 1,
                title: '新建作业',
                area: [winWidth + 'px', winHeight + "px"],
                shade: 0, //去掉遮罩
                resize: false,
                btnAlign: 'c',
                content: $("#newInstanceDiv"),
                btn: btns,
                btn1: function (index, layero) {
                    Instance.saveJob(index, code, instanceId, false)
                },
                btn2: function (index, layero) {
                    Instance.saveJob(index, code, instanceId, true)
                }
            });
        },

        saveJob : function(index, code, instanceId, isRun) {
            let data = form.val('newInstanceForm');
            data.jobText = $.trim(jobTextEditor.getValue());
            data.jobConfig = $.trim(jobConfigEditor.getValue());

            if (!data.name) {
                toastr.error("请填写作业名称");
                return
            }
            if (!data.clusterCode) {
                toastr.error("请选择集群");
                return
            }
            if (!data.jobText) {
                toastr.error("作业内容不能为空");
                return
            }

            data.id = instanceId;
            data.code = code;
            data.isRun = isRun;
            $.ajax({
                async: true,
                type: "POST",
                url: '/instance/saveJobInstance',
                data: data,
                success: function (result) {
                    if (result.success) {
                        layer.close(index);
                        Instance.refresh();
                        toastr.error("保存成功");
                    } else {
                        toastr.error(result.message);
                    }
                },
            });
        },

        viewLog : function(instanceCode, scheduleTime, refresh) {
            if (nodeLogEditor == null) {
                nodeLogEditor = ace.edit("nodeLogArea");
                nodeLogEditor.setReadOnly(true);
                nodeLogEditor.setTheme("ace/theme/idle_fingers");
                nodeLogEditor.getSession().setMode("ace/mode/log");
                $('#nodeLogArea').height(600 + "px");
                nodeLogEditor.resize();
            }

            nodeLogEditor.setValue("")
            $.ajax({
                async: false,
                type: "GET",
                url: '/instance/queryInstanceLog?instanceCode=' + instanceCode + "&scheduleDate=" + scheduleTime.substring(0, 10),
                success: function (result) {
                    if (result.success) {
                        nodeLogEditor.setValue(result.data)
                        nodeLogEditor.clearSelection();
                    } else {
                        nodeLogEditor.setValue(result.message)
                        nodeLogEditor.clearSelection();
                    }
                },
            });

            if (refresh) {
                return
            }

            var index = layer.open({
                type: 1,
                title:'作业日志',
                area: '1000px',
                shade:0, //去掉遮罩
                content: $("#nodeLogDiv"),
                btn: ['刷新', '下载日志', '查看第一行', '查看最后一行'],
                btn1: function(index, layero) {
                    Instance.viewLog(instanceCode, scheduleTime, true)
                    return false
                },
                btn2: function(index, layero) {
                    let url = "/instance/downloadInstanceLog?instanceCode=" + instanceCode + "&scheduleDate=" + scheduleTime.substring(0, 10);
                    window.open(url, '_blank');
                    return false
                },
                btn3: function(index, layero) {
                    nodeLogEditor.scrollToLine(0);
                    return false
                },
                btn4: function(index, layero) {
                    Instance.viewLog(instanceCode, scheduleTime, true)
                    let row = nodeLogEditor.session.getLength() - 1
                    nodeLogEditor.scrollToLine(row);
                    return false
                },
                resizing: function(layero){
                    $('#nodeLogArea').height(($("#layui-layer" + index).height() - 110) + "px");
                    $('#nodeLogArea').width(($("#layui-layer" + index).width()) + "px");
                    nodeLogEditor.resize();
                }
            });

            $('#nodeLogArea').height(($("#layui-layer" + index).height() - 110) + "px");
            $('#nodeLogArea').width(($("#layui-layer" + index).width()) + "px");
            nodeLogEditor.resize();
        },

        downloadLog : function (instanceCode, scheduleTime) {
            var url = "/instance/downloadInstanceLog?instanceCode=" + instanceCode + "&scheduleDate=" + scheduleTime.substring(0, 10);
            window.open(url, '_blank');
        },

        viewCode : function(instanceCode) {
            if (nodeCodeEditor == null) {
                nodeCodeEditor = ace.edit("nodeCodeArea");
                nodeCodeEditor.setReadOnly(true);
                nodeCodeEditor.setTheme("ace/theme/idle_fingers");
                nodeCodeEditor.getSession().setMode("ace/mode/text");
                $('#nodeCodeArea').height(600 + "px");
                nodeCodeEditor.resize();
            }

            nodeCodeEditor.setValue("")
            $.ajax({
                async: true,
                type: "GET",
                url: '/instance/queryJobText?instanceCode=' + instanceCode,
                success: function (result) {
                    if (result.success) {
                        var message = result.data;
                        nodeCodeEditor.setValue(message)
                        nodeCodeEditor.clearSelection();
                    } else {
                        toastr.error(result.message);
                    }
                },
            });

            var codeIndex = layer.open({
                type: 1,
                title:'作业代码',
                area: '1000px',
                shade:0,//去掉遮罩
                content: $("#nodeCodeDiv"),
                resizing: function(layero) {
                    $('#nodeCodeArea').height(($("#layui-layer"+codeIndex).height()) + "px");
                    $('#nodeCodeArea').width(($("#layui-layer"+codeIndex).width()) + "px");
                    nodeCodeEditor.resize();
                }
            });
        },

        killJob : function(instanceCode) {
            layer.confirm('确认终止运行作业吗？', {
                btn: ['确认','取消'], //按钮
                title: '提示'
            }, function(ind){
                layer.close(ind);

                $.ajax({
                    async: true,
                    type: "POST",
                    url: '/instance/killJob?instanceCode=' + instanceCode,
                    success: function (result) {
                        if (result.success) {
                            toastr.success("成功终止作业: " + instanceCode)
                        } else {
                            toastr.error(result.message)
                        }
                        Instance.refresh();
                    }
                });
            });
        },

        deleteInstance : function (instanceCode) {
            layer.confirm('确认删除当前实例?', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    async: true,
                    type: "POST",
                    url: '/instance/delete?instanceCode=' + instanceCode,
                    success: function (data) {
                        if (data.success) {
                            layer.msg('删除成功', {icon: 1});
                            Instance.refresh();
                        }
                    },
                });
            });
        },

        queryInstanceStatistics : function() {
            $.ajax({
                async: true,
                type: "POST",
                url: '/instance/queryInstanceStatistics',
                success: function (result) {
                    if (result.success) {
                        $("#waitingCount").text(result.data.waitingCount);
                        $("#runningCount").text(result.data.runningCount);
                        $("#lastDayFinishedCount").text(result.data.lastDayFinishedCount);
                        $("#lastDayFailedCount").text(result.data.lastDayFailedCount);
                        $("#lastDayStoppedCount").text(result.data.lastDayStoppedCount);
                    }
                },
            });
        },

        refresh : function() {
            table.reload('instance-table');
            Instance.queryInstanceStatistics();
        }
    };
}();

$(document).ready(function () {
    Instance.init();
    setInterval(Instance.refresh, 30000);

    //防止页面后退
    history.pushState(null, null, document.URL);
    window.addEventListener('popstate', function () {
        history.pushState(null, null, document.URL);
    });
});
