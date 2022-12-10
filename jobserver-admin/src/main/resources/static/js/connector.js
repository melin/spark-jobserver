var Connector = function () {
    let table = layui.table;
    let form = layui.form;
    let dropdown = layui.dropdown;

    return {
        init: function () {
            let cols = [
                [{
                    title: '序号',
                    type: 'numbers'
                },
                    {
                        title: 'Code',
                        field: 'code',
                        align: 'center',
                        width: 90
                    },
                    {
                        title: 'Name',
                        field: 'name',
                        align: 'left',
                        width: 80
                    },
                    {
                        title: 'Type',
                        field: 'connectorType',
                        align: 'left',
                        width: 80
                    },
                    {
                        title: 'User',
                        field: 'username',
                        align: 'left',
                        width: 100
                    },
                    {
                        title: 'URL',
                        field: 'jdbcUrl',
                        align: 'left',
                        width: 200
                    },
                    {
                        title: '更新时间',
                        field: 'gmtModified',
                        align: 'left',
                        width: 120
                    },
                    {
                        title: '操作',
                        toolbar: '#connector-bar',
                        align: 'right',
                        fixed: 'right',
                        width: 50
                    }
                ]
            ]

            table.render({
                elem: '#connector-table',
                url: '/connector/queryConnectors',
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
                defaultToolbar: [],
                done: function(res, curr, count) {
                    for (var i = 0; i < res.data.length; i++) {
                        const row = res.data[i];
                        const menus = []
                        menus.push({title: '编辑集群', id: "editorConnector", connectorId: row.id, code: row.code});
                        menus.push({title: '删除集群', id: "deleteConnector", connectorId: row.id, code: row.code});

                        dropdown.render({
                            elem: '#opt_' + row.id,
                            data: menus,
                            id: "#opt_" + row.id,
                            click: function(obj) {
                                if (obj.id === "editorConnector") {
                                    Connector.newConnectorWin(obj.connectorId)
                                } else if (obj.id === "deleteConnector") {
                                    Connector.deleteConnector(obj.connectorId, obj.code)
                                }
                            }
                        });
                    }
                }
            });

            table.on('toolbar(connector-table)', function(obj) {
                if (obj.event === 'refresh') {
                    Connector.refresh();
                }
            });

            form.on('submit(user-query)', function(data) {
                table.reload('connector-table', {
                    where: data.field
                })
                return false;
            });
        },

        newConnectorWin : function(connectorId) {
            if (connectorId) {
                $.ajax({
                    async: true,
                    type : "GET",
                    url: '/connector/queryConnector',
                    data: { connectorId: connectorId },
                    success: function (result) {
                        if (result.success) {
                            let data = result.data;
                            form.val('connectorForm', data);

                            $("#connectorType").attr("readonly", "readonly");
                            $("#code").attr("readonly", "readonly");
                        }
                    }
                })
            } else {
                form.val('newClusterForm', {});
                $("#connectorType").attr("readonly", false);
                $("#code").attr("readonly", false);
            }

            var index = layer.open({
                type: 1,
                title: 'New Connector',
                area: ['600px', "490px"],
                shade: 0, //去掉遮罩
                resize: false,
                btnAlign: 'c',
                content: $("#newConnectorDiv"),
                btn: ['Test Connection', 'Save'],
                btn1: function(index, layero) {
                    let params = form.val('connectorForm')
                    params.id = connectorId;
                    $.ajax({
                        type: "POST",
                        data: params,
                        url: "/connector/testConnection",
                        success: function (result) {
                            if (result.success) {
                                $("#testresult").html('<span style="color: #00FF00">测试成功！数据库信息: '
                                    + result.data.databaseProductName + "(" + result.data.databaseProductVersion + ")</span>")
                            } else {
                                $("#testresult").html('<span style="color: #e36209">' + result.message + '</span>');
                            }

                            $("body").mLoading("hide");
                        }
                    });
                },
                btn2: function(index, layero) {
                    let params = form.val('connectorForm')
                    $.ajax({
                        type: "POST",
                        data: params,
                        url: "/connector/saveConnector",
                        success: function (result) {
                            if (result.success) {
                                toastr.success("保存成功");
                                Connector.refresh();
                            } else {
                                toastr.error(result.message);
                            }
                        }
                    });
                }
            });
        },

        deleteConnector : function (connectorId, code) {
            layer.confirm('确定关闭: ' + code + " ?", {
                btn: ['确认','取消'],
                title: '提示'
            }, function (index) {
                layer.close(index);
                $.ajax({
                    async: true,
                    type : "POST",
                    url: '/connector/deleteConnector',
                    data: { connectorId: connectorId },
                    success: function (result) {
                        if (result.success) {
                            toastr.success("成功删除Connector: " + clusterCode)
                            Connector.refresh();
                        } else {
                            toastr.error(result.message)
                        }
                    }
                })
            })
        },

        refresh : function() {
            table.reload('connector-table');
        }
    };
}();

$(document).ready(function () {
    Connector.init();
});
