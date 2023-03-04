var Cluster = function () {
    let winWidth, winHeight;
    let table = layui.table;
    let form = layui.form;
    let element = layui.element;
    let dropdown = layui.dropdown;
    const maxInstanceCount = $("#maxInstanceCount").val();
    let editClusterId;

    let sparkCompleter = {
        identifierRegexps: [/[a-zA-Z_0-9\.\$\-\u00A2-\uFFFF]/], //解决输入点启动提示
        getCompletions: function(editor, session, pos, prefix, callback) {
            let currentLine = session.getLine(pos.row)
            if (currentLine.indexOf("=") > 0) { callback(null, []); return }
            if (prefix.length === 0) { callback(null, []); return }

            SPARK_CONFIG_OPTIONS.push(...HUDI_CONFIG_OPTIONS);
            callback(null, SPARK_CONFIG_OPTIONS)
        }
    }
    let langTools = ace.require("ace/ext/language_tools");
    langTools.setCompleters([sparkCompleter]);

    let jobserverEditor, sparkEditor, coreEditor, hdfsEditor, yarnEditor, hiveEditor,
        kubernetesEditor, driverPodTemplateEditor, executorPodTemplateEditor, krb5ConfEditor;

    let keytabBase64 = "";
    let kerberosFileName = "";
    var handleFileSelect = function(evt) {
        var files = evt.target.files;
        var file = files[0];
        kerberosFileName = file.name;

        if (files && file) {
            var reader = new FileReader();
            reader.onload = function(readerEvt) {
                var binaryString = readerEvt.target.result;
                keytabBase64 = btoa(binaryString);
            };
            reader.readAsBinaryString(file);
        }
    };

    return {
        init: function () {
            winWidth = $(window).width() * 0.95;
            winHeight = $(window).height() * 0.95;

            $('#schedulerTypeTip').on('click', function() {
                layer.tips('<li>Yarn: 需要填写core-site.xml, hdfs-sitexml, yarn-site.xml配置，hive-site.xml是可选的</li>'
                    + '<li>Kubenetes: core-site.xml, hdfs-sitexml, yarn-site.xml, hive-site.xml, Kubernetes Config 用于访问k8s 集群配置，必填写</li>',
                    '#schedulerTypeTip', {time: 2000});
            });

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
                            if (kerberosEnabled) {
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
                        templet: "#statusTpl"
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
                    Cluster.deleteCluster(data.id, data.code)
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

            form.on('switch(status)', function(data){
                let clusterId = data.value;
                let status = this.checked;

                $.ajax({
                    type: 'POST',
                    url: '/cluster/updateStatus',
                    data: {"clusterId": clusterId, "status": status},
                    beforeSend:function(){
                        index = layer.msg('正在切换中，请稍候', {icon: 16,time:false,shade:0.8});
                    },
                    error: function(data){
                        layer.msg('数据异常，操作失败！');
                    },
                    success: function(result){
                        if (result.success) {
                            layer.msg('操作成功!');
                        } else {
                            layer.msg('操作失败: ' + result.message);
                            Cluster.refresh();
                        }},
                    dataType:'JSON'
                });
            });

            jobserverEditor = Cluster.getEditor(jobserverEditor, "jobserverEditor", "ace/mode/properties");
            sparkEditor = Cluster.getEditor(sparkEditor, "sparkEditor", "ace/mode/properties");
            coreEditor = Cluster.getEditor(coreEditor, "coreEditor", "ace/mode/xml");
            hdfsEditor = Cluster.getEditor(hdfsEditor, "hdfsEditor", "ace/mode/xml");
            hiveEditor = Cluster.getEditor(hiveEditor, "hiveEditor", "ace/mode/xml");

            form.on('select(schedulerType)', function (data) {
                Cluster.changeSchedulerTypeTab(data.value)
            });

            form.on('select(kerberosEnabled)', function (data) {
                Cluster.changeKerberosTab(data.value)
            });

            if (window.File && window.FileReader && window.FileList && window.Blob) {
                document.getElementById('kerberosKeytab')
                    .addEventListener('change', handleFileSelect, false);
            } else {
                alert('The File APIs are not fully supported in this browser.');
            }
        },

        changeSchedulerTypeTab : function (schedulerType) {
            if (schedulerType === "kubernetes") {
                element.tabDelete('config_tabs', 'yarn_tab')
                element.tabDelete('config_tabs', 'kubernetes_tab')
                element.tabDelete('config_tabs', 'driverPodTemplate_tab')
                element.tabDelete('config_tabs', 'executorPodTemplate_tab')

                element.tabAdd('config_tabs', {id: 'kubernetes_tab', title: 'K8s.conf',
                    content: '<div id="kubernetesEditor" style="width: 100%;" class="editor"></div>'});
                kubernetesEditor = Cluster.getEditor(kubernetesEditor, "kubernetesEditor", "ace/mode/yaml");

                element.tabAdd('config_tabs', {id: 'driverPodTemplate_tab', title: 'Driver Pod Template',
                    content: '<div id="driverPodTemplateEditor" style="width: 100%;" class="editor"></div>'});
                driverPodTemplateEditor = Cluster.getEditor(driverPodTemplateEditor, "driverPodTemplateEditor", "ace/mode/yaml");

                element.tabAdd('config_tabs', {id: 'executorPodTemplate_tab', title: 'Executor Pod Template',
                    content: '<div id="executorPodTemplateEditor" style="width: 100%;" class="editor"></div>'});
                executorPodTemplateEditor = Cluster.getEditor(executorPodTemplateEditor, "executorPodTemplateEditor", "ace/mode/yaml");
            } else {
                element.tabDelete('config_tabs', 'yarn_tab')
                element.tabDelete('config_tabs', 'kubernetes_tab')
                element.tabDelete('config_tabs', 'driverPodTemplate_tab')
                element.tabDelete('config_tabs', 'executorPodTemplate_tab')

                element.tabAdd('config_tabs', {id: 'yarn_tab', title: 'yarn-site.xml',
                    content: '<div id="yarnEditor" style="width: 100%;" class="editor"></div>'});
                yarnEditor = Cluster.getEditor(yarnEditor, "yarnEditor", "ace/mode/xml");
            }
        },

        changeKerberosTab : function (kerberosEnabled) {
            if (kerberosEnabled === "true") {
                element.tabDelete('config_tabs', 'kerberos_tab')
                element.tabAdd('config_tabs', {id: 'kerberos_tab', title: 'Krb5.conf',
                    content: '<div id="krb5ConfEditor" style="width: 100%;" class="editor"></div>'});
                krb5ConfEditor = Cluster.getEditor(krb5ConfEditor, "krb5ConfEditor", "ace/mode/properties");

                $(".kerberos_span").show()
            } else {
                element.tabDelete('config_tabs', 'kerberos_tab')
                $(".kerberos_span").hide()
            }
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
                editClusterId = clusterId;
                $.ajax({
                    async: true,
                    type : "GET",
                    url: '/cluster/queryCluster',
                    data: { clusterId: clusterId },
                    success: function (result) {
                        if (result.success) {
                            let data = result.data;
                            if (data.kerberosEnabled) {
                                Cluster.changeKerberosTab("true")
                                Cluster.setEditorValue(krb5ConfEditor, data.kerberosConfig)
                                data.kerberosEnabled = "true";
                                $("#kerberosDown").html(data.kerberosFileName);
                            } else {
                                Cluster.changeKerberosTab("false")
                                data.kerberosEnabled = "false";
                            }

                            if (data.status) {
                                data.status = "true";
                            } else {
                                data.status = "false";
                            }

                            form.val('newClusterForm', data);
                            Cluster.setEditorValue(jobserverEditor, data.jobserverConfig)
                            Cluster.setEditorValue(sparkEditor, data.sparkConfig)
                            Cluster.setEditorValue(coreEditor, data.coreConfig)
                            Cluster.setEditorValue(hdfsEditor, data.hdfsConfig)
                            Cluster.setEditorValue(hiveEditor, data.hiveConfig)

                            Cluster.changeSchedulerTypeTab(data.schedulerType)
                            if (data.schedulerType === "yarn") {
                                Cluster.setEditorValue(yarnEditor, data.yarnConfig)
                            } else {
                                Cluster.setEditorValue(kubernetesEditor, data.kubernetesConfig)
                                Cluster.setEditorValue(driverPodTemplateEditor, data.driverPodTemplate)
                                Cluster.setEditorValue(executorPodTemplateEditor, data.executorPodTemplate)
                            }
                        }
                    }
                })

                $("#schedulerType").attr("disabled","disabled");
                form.render('select');
            } else {
                form.val('newClusterForm', {code: "", name: "", yarnQueueName: "default"});
                Cluster.setEditorValue(jobserverEditor, $("#confDefaultValue").val())
                Cluster.setEditorValue(sparkEditor, "")
                Cluster.setEditorValue(coreEditor, "")
                Cluster.setEditorValue(hdfsEditor, "")
                Cluster.setEditorValue(hiveEditor, "")

                Cluster.changeSchedulerTypeTab("yarn")
                Cluster.setEditorValue(yarnEditor, "")
                Cluster.changeKerberosTab("false")

                $("#schedulerType").removeAttr("disabled");
                form.render('select');
            }

            var index = layer.open({
                type: 1,
                title: 'New Cluster',
                area: [winWidth + 'px', winHeight + "px"],
                shade: 0, //去掉遮罩
                resize: false,
                btnAlign: 'c',
                content: $("#newClusterDiv"),
                btn: ['保存', '取消'],
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
                    let hiveConfig = $.trim(hiveEditor.getValue());

                    let yarnConfig = "";
                    let kubernetesConfig = "";
                    let driverPodTemplate = "";
                    let executorPodTemplate = "";
                    if (data.schedulerType === "yarn") {
                        yarnConfig = $.trim(yarnEditor.getValue());
                    } else {
                        kubernetesConfig = $.trim(kubernetesEditor.getValue());
                        driverPodTemplate = $.trim(driverPodTemplateEditor.getValue());
                        executorPodTemplate = $.trim(executorPodTemplateEditor.getValue());
                    }

                    data.id = clusterId
                    data.jobserverConfig = jobserverConfig
                    data.sparkConfig = sparkConfig
                    data.coreConfig = coreConfig
                    data.hdfsConfig = hdfsConfig
                    data.hiveConfig = hiveConfig
                    data.yarnConfig = yarnConfig
                    data.kubernetesConfig = kubernetesConfig
                    data.driverPodTemplate = driverPodTemplate
                    data.executorPodTemplate = executorPodTemplate
                    data.keytabBase64 = keytabBase64
                    data.kerberosFileName = kerberosFileName

                    if (data.kerberosEnabled) {
                        data.kerberosConfig = $.trim(krb5ConfEditor.getValue());
                    }

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

        deleteCluster : function (clusterId, clusterCode) {
            layer.confirm('确定关闭: ' + clusterCode + " ?", {
                btn: ['确认','取消'],
                title: '提示'
            }, function (index) {
                layer.close(index);
                $.ajax({
                    async: true,
                    type : "POST",
                    url: '/cluster/deleteCluster',
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

        downloadKeytab: function () {
            var url = "/cluster/downloadKeytab?clusterId=" + editClusterId;
            window.open(url, '_blank');
        },

        refresh : function() {
            table.reload('cluster-table');
        }
    };
}();

$(document).ready(function () {
    Cluster.init();
});
