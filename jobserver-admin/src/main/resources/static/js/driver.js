var Driver = function () {
    let table = layui.table;
    let form = layui.form;
    let dropdown = layui.dropdown;
    let clusterMap = new Map();

    return {
        init: function () {
            $("#clusterVals option").each(function() {
                clusterMap.set($(this).val(), $(this).text())
            });

            let cols = [
                [{
                    title: '序号',
                    type: 'numbers'
                },
                    {
                        title: '集群',
                        field: 'clusterCode',
                        align: 'center',
                        width: 120,
                        templet: function(record) {
                            const clusterCode = record.clusterCode;
                            if (clusterCode) {
                                return clusterMap.get(clusterCode)
                            } else {
                                return "";
                            }
                        }
                    },
                    {
                        title: 'DriverID',
                        field: 'id',
                        align: 'left',
                        width: 80,
                        templet: function(record) {
                            const serverIp = record.serverIp;
                            const serverPort = record.serverPort;
                            if (serverIp && serverPort) {
                                return '<a href="http://' + serverIp + ':' + serverPort + '/gitInfo" target="_blank">' + record.id + '</a>';
                            } else {
                                return record.id;
                            }
                        }
                    },
                    {
                        title: 'ApplicationID',
                        field: 'applicationId',
                        align: 'left',
                        width: 220,
                        templet: function(record) {
                            const applicationId = record.applicationId;
                            const sparkYarnProxyUri = record.sparkYarnProxyUri;
                            if (applicationId && "null" !== applicationId) {
                                return '<a href="' + sparkYarnProxyUri + '/proxy/' + applicationId + '" target="_blank">' + applicationId + '</a>';
                            } else {
                                return "";
                            }
                        }
                    },
                    {
                        title: '计算类型',
                        field: 'computeType',
                        align: 'left',
                        width: 80
                    },
                    {
                        title: '状态',
                        field: 'status',
                        align: 'left',
                        width: 80,
                        templet: function(record) {
                            const status = record.status;
                            if (status === "init") {
                                return '<span style="font-weight:bold; color: #FF5722">初始化中</span>'
                            } else if (status === "idle") {
                                return '<span style="font-weight:bold;">空闲</span>'
                            } else if (status === "running") {
                                return '<span style="font-weight:bold;color: #FFB800">运行中</span>'
                            } else if (status === "finished") {
                                return '<span style="font-weight:bold;color: #5FB878">完成</span>'
                            } else if (status === "locked") {
                                return '<span style="font-weight:bold;color: #FFB800">锁定</span>'
                            }
                            return status;
                        }
                    },
                    {
                        title: '共享',
                        field: 'shareDriver',
                        align: 'left',
                        width: 70,
                        templet: function(record) {
                            const shareDriver = record.shareDriver;
                            const instanceCount = record.instanceCount;

                            if (shareDriver) {
                                return '是(' + instanceCount + "/30)";
                            } else {
                                return '';
                            }
                        }
                    },
                    {
                        title: '创建时间',
                        field: 'gmtCreated',
                        align: 'left',
                        width: 130
                    },
                    {
                        title: 'CPU',
                        field: 'serverCores',
                        align: 'left',
                        width: 60,
                    },
                    {
                        title: '内存(G)',
                        field: "serverMemory",
                        align: 'left',
                        width: 80,
                        templet: function(record) {
                            const serverMemory = record.serverMemory;
                            return Math.floor(serverMemory / 1024 * 100) / 100 + "G"
                        }
                    },
                    {
                        title: 'Yarn队列',
                        align: 'left',
                        field: "yarnQueue",
                        width: 100,
                    },
                    {
                        title: '运行实例',
                        align: 'left',
                        field: 'instanceCode',
                        width: 100,
                    },
                    {
                        title: '操作',
                        toolbar: '#driver-bar',
                        align: 'right',
                        fixed: 'right',
                        width: 100
                    }
                ]
            ]

            table.render({
                elem: '#driver-table',
                url: '/driver/queryDrivers',
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
                defaultToolbar: [],
            });

            table.on('tool(driver-table)', function(obj) {
                let data = obj.data;
                if (obj.event === 'download') {
                    Driver.downloadYarnLog(data.id, data.applicationId)
                } else if (obj.event === 'remove') {
                    Driver.closeDriver(data.id, data.applicationId)
                }
            });

            table.on('toolbar(driver-table)', function(obj) {
                if (obj.event === 'refresh') {
                    Driver.refresh();
                }
            });

            form.on('submit(user-query)', function(data) {
                table.reload('driver-table', {
                    where: data.field
                })
                return false;
            });
        },

        downloadYarnLogWin : function() {
            var index = layer.open({
                type: 1,
                title: '下载Yarn日志',
                area: ['400px', "160px"],
                shade: 0, //去掉遮罩
                resize: false,
                btnAlign: 'c',
                content: $("#downloadYarnLogDiv"),
                btn: ['保存'],
                btn1: function(index, layero) {
                    let appId = form.val('yarnLogForm').appId
                    if (appId) {
                        let url = "/driver/downloadYarnLog?applicationId=" + appId;
                        Driver.open(url, '_blank');
                        layer.close(index);
                    } else {
                        toastr.error("请填写Yarn ApplicationId");
                    }
                }
            });
        },

        downloadYarnLog : function (driverId, applicationId) {
            var url = "/driver/downloadYarnLog?applicationId=" + applicationId;
            window.open(url, '_blank');
        },

        closeDriver : function (driverId, applicationId) {
            let id = driverId;
            if (applicationId) {
                id = applicationId;
            }
            layer.confirm('确定关闭: ' + id + " ?", {
                btn: ['确认','取消'],
                title: '提示'
            }, function (index) {
                layer.close(index);
                $.ajax({
                    async: true,
                    type : "POST",
                    url: '/driver/killDriver',
                    data: { driverId: driverId },
                    success: function (result) {
                        if (result.success) {
                            toastr.success("成功关闭Server: " + id)
                            table.reload('driver-table');
                        } else {
                            toastr.error(result.message)
                        }
                    }
                })
            })
        },

        refresh : function() {
            table.reload('driver-table');
        }
    };
}();

$(document).ready(function () {
    Driver.init();

    setInterval(Driver.refresh, 10000);
});
