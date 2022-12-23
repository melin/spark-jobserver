var Cluster = function () {
    let winWidth, winHeight;
    let table = layui.table;
    let form = layui.form;
    let dropdown = layui.dropdown;
    const maxInstanceCount = $("#maxInstanceCount").val();

    let sparkCompleter = {
        identifierRegexps: [/[a-zA-Z_0-9\.\$\-\u00A2-\uFFFF]/], //解决输入点启动提示
        getCompletions: function(editor, session, pos, prefix, callback) {
            let currentLine = session.getLine(pos.row)
            if (currentLine.indexOf("=") > 0) { callback(null, []); return }
            if (prefix.length === 0) { callback(null, []); return }

            callback(null, SPARK_CONFIG_OPTIONS)
        }
    }
    let langTools = ace.require("ace/ext/language_tools");
    langTools.setCompleters([sparkCompleter]);

    let jobserverEditor, sparkEditor, coreEditor, hdfsEditor, yarnEditor, hiveEditor;

    return {
        init: function () {
            winWidth = $(window).width() * 0.95;
            winHeight = $(window).height() * 0.95;

            let cols = [
                [{
                    title: '序号',
                    type: 'numbers'
                },
                    {
                        title: '集群Code',
                        field: 'code',
                        align: 'left',
                        width: 120
                    },
                    {
                        title: '集群名称',
                        field: 'name',
                        align: 'left',
                        width: 130,
                    },
                    {
                        title: '调度类型',
                        field: 'schedulerType',
                        align: 'left',
                        width: 100,
                    },
                    {
                        title: '开启kerberos',
                        field: 'kerberosEnabled',
                        align: 'left',
                        width: 100,
                        templet: function(record) {
                            const kerberosEnabled = record.kerberosEnabled;
                            if (kerberosEnabled === 1) {
                                return '<span style="font-weight:bold; color: #5FB878">启用</span>'
                            } else {
                                return '<span style="font-weight:bold;color: #FF5722">关闭</span>'
                            }
                        }
                    },
                    {
                        title: '状态',
                        field: 'status',
                        align: 'left',
                        width: 80,
                        templet: function(record) {
                            const status = record.status;
                            if (status) {
                                return '<span style="font-weight:bold; color: #5FB878">启用</span>'
                            } else {
                                return '<span style="font-weight:bold;color: #FF5722">关闭</span>'
                            }
                        }
                    },
                    {
                        title: '更新时间',
                        field: 'gmtModified',
                        align: 'left',
                        width: 150
                    },
                    {
                        title: '操作',
                        toolbar: '#cluster-bar',
                        align: 'right',
                        width: 100,
                        fixed: "right"
                    }
                ]
            ]

            table.render({
                elem: '#cluster-table',
                url: '/cluster/queryClusters',
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
                defaultToolbar: []
            });

            table.on('tool(cluster-table)', function(obj) {
                let data = obj.data;
                if (obj.event === 'remove') {
                    Cluster.closeCluster(data.id, data.code)
                } else if (obj.event === 'edit') {
                    Cluster.newClusterWin(data.id)
                }
            });

            table.on('toolbar(cluster-table)', function(obj) {
                if (obj.event === 'refresh') {
                    Cluster.refresh();
                }
            });

            form.on('submit(user-query)', function(data) {
                table.reload('cluster-table', {
                    where: data.field
                })
                return false;
            });

            jobserverEditor = Cluster.getEditor(jobserverEditor, "jobserverEditor", "ace/mode/properties");
            sparkEditor = Cluster.getEditor(sparkEditor, "sparkEditor", "ace/mode/properties");
            coreEditor = Cluster.getEditor(coreEditor, "coreEditor", "ace/mode/xml");
            hdfsEditor = Cluster.getEditor(hdfsEditor, "hdfsEditor", "ace/mode/xml");
            yarnEditor = Cluster.getEditor(yarnEditor, "yarnEditor", "ace/mode/xml");
            hiveEditor = Cluster.getEditor(hiveEditor, "hiveEditor", "ace/mode/xml");
            //let kerberosEditor = Cluster.getEditor(kerberosEditor, "kerberosEditor", "ace/mode/properties");
        },

        getEditor: function(editor, editorId, mode) {
            editor = ace.edit(editorId);
            if ("sparkEditor" === editorId) {
                editor.commands.on("afterExec", function (e) {
                    if (e.command.name == "insertstring" && /^[\w.]$/.test(e.args)) {
                        editor.execCommand("startAutocomplete");
                    }
                });
            }

            editor.setTheme("ace/theme/cobalt");
            editor.getSession().setMode(mode);
            $('#' + editorId).height((winHeight - 285) + "px");
            editor.resize();

            if ("sparkEditor" === editorId) {
                editor.setOptions({
                    enableBasicAutocompletion: true,
                    enableSnippets: true,
                    enableLiveAutocompletion: true
                });
            }
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

        newClusterWin : function(clusterId) {
            if (clusterId) {
                $.ajax({
                    async: true,
                    type : "GET",
                    url: '/cluster/queryCluster',
                    data: { clusterId: clusterId },
                    success: function (result) {
                        if (result.success) {
                            let data = result.data;
                            if (data.kerberosEnabled) {
                                data.kerberosEnabled = 1;
                            } else {
                                data.kerberosEnabled = 0;
                            }
                            if (data.status) {
                                data.status = 1;
                            } else {
                                data.status = 0;
                            }

                            form.val('newClusterForm', data);
                            Cluster.setEditorValue(jobserverEditor, data.jobserverConfig)
                            Cluster.setEditorValue(sparkEditor, data.sparkConfig)
                            Cluster.setEditorValue(coreEditor, data.coreConfig)
                            Cluster.setEditorValue(hdfsEditor, data.hdfsConfig)
                            Cluster.setEditorValue(yarnEditor, data.yarnConfig)
                            Cluster.setEditorValue(hiveEditor, data.hiveConfig)
                        }
                    }
                })
            } else {
                form.val('newClusterForm', {code: "", name: "", yarnQueueName: "default"});
                Cluster.setEditorValue(jobserverEditor, $("#confDefaultValue").val())
                Cluster.setEditorValue(sparkEditor, "")
                Cluster.setEditorValue(coreEditor, "")
                Cluster.setEditorValue(hdfsEditor, "")
                Cluster.setEditorValue(yarnEditor, "")
                Cluster.setEditorValue(hiveEditor, "")
            }

            var index = layer.open({
                type: 1,
                title: 'New Cluster',
                area: [winWidth + 'px', winHeight + "px"],
                shade: 0, //去掉遮罩
                resize: false,
                btnAlign: 'c',
                content: $("#newClusterDiv"),
                btn: ['Save'],
                zIndex: 1111,
                btn1: function(index, layero) {
                    let data = form.val('newClusterForm');
                    if (!data.code) {
                        toastr.error("集群code不能为空");
                        return
                    }
                    if (!data.name) {
                        toastr.error("集群名称不能为空");
                        return
                    }

                    let jobserverConfig = $.trim(jobserverEditor.getValue());
                    let sparkConfig = $.trim(sparkEditor.getValue());
                    let coreConfig = $.trim(coreEditor.getValue());
                    let hdfsConfig = $.trim(hdfsEditor.getValue());
                    let yarnConfig = $.trim(yarnEditor.getValue());
                    let hiveConfig = $.trim(hiveEditor.getValue());

                    data.id = clusterId
                    data.jobserverConfig = jobserverConfig
                    data.sparkConfig = sparkConfig
                    data.coreConfig = coreConfig
                    data.hdfsConfig = hdfsConfig
                    data.yarnConfig = yarnConfig
                    data.hiveConfig = hiveConfig
                    $.ajax({
                        async: true,
                        type: "POST",
                        url: '/cluster/saveCluster',
                        data: data,
                        success: function (result) {
                            if (result.success) {
                                toastr.success("保存成功");
                                Cluster.refresh();
                            } else {
                                toastr.error(result.message);
                            }
                        }
                    });
                }
            });
        },

        closeCluster : function (clusterId, clusterCode) {
            layer.confirm('确定关闭: ' + clusterCode + " ?", {
                btn: ['确认','取消'],
                title: '提示'
            }, function (index) {
                layer.close(index);
                $.ajax({
                    async: true,
                    type : "POST",
                    url: '/cluster/closeCluster',
                    data: { clusterId: clusterId },
                    success: function (result) {
                        if (result.success) {
                            toastr.success("成功关闭集群: " + clusterCode)
                            Cluster.refresh();
                        } else {
                            toastr.error(result.message)
                        }
                    }
                })
            })
        },

        refresh : function() {
            table.reload('cluster-table');
        }
    };
}();

$(document).ready(function () {
    Cluster.init();
});
