package io.github.melin.spark.jobserver.web.controller;

import com.gitee.melin.bee.core.jdbc.relational.DataSourceInfo;
import com.gitee.melin.bee.core.support.Pagination;
import com.gitee.melin.bee.core.support.Result;
import com.google.common.collect.Lists;
import io.github.melin.spark.jobserver.core.entity.DataConnector;
import io.github.melin.spark.jobserver.core.service.DataConnectorService;
import io.github.melin.spark.jobserver.core.util.AESUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

@Controller
public class ConnectorController {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectorController.class);

    @Autowired
    private DataConnectorService connectorService;

    @RequestMapping("/connector")
    public String home(ModelMap model) {
        return "connector";
    }

    @RequestMapping("/connector/queryConnectors")
    @ResponseBody
    public Pagination<DataConnector> queryConnectors(String connectorType, String name,
                                                     int page, int limit, HttpServletRequest request) {
        String sort = request.getParameter("sort");
        String orderStr = request.getParameter("order");

        Order order = Order.desc("gmtModified");
        if (StringUtils.isNotEmpty(orderStr)) {
            if ("asc".equals(order)) {
                order = Order.asc(sort);
            } else {
                order = Order.desc(sort);
            }
        }

        List<String> params = Lists.newArrayList();
        List<Object> values = Lists.newArrayList();
        if (StringUtils.isNotBlank(connectorType)) {
            params.add("connectorType");
            values.add(connectorType);
        }

        if (StringUtils.isNotBlank(name)) {
            params.add("code");
            values.add(Restrictions.or(Restrictions.like("code", name, MatchMode.ANYWHERE),
                    Restrictions.like("name", name, MatchMode.ANYWHERE)));
        }
        return connectorService.findPageByNamedParamAndOrder(params, values,
                Lists.newArrayList(order), page, limit);
    }

    @RequestMapping("/connector/queryConnector")
    @ResponseBody
    public Result<DataConnector> queryConnector(Long connectorId) {
        try {
            DataConnector connector = connectorService.getEntity(connectorId);
            return Result.successDataResult(connector);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @PostMapping("/connector/testConnection")
    @ResponseBody
    public Result<DataSourceInfo> testConnection(DataConnector dataConnector) {
        /*String hostName = connector.get();
        int port = connector.getPort();
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        try (Socket socket = new Socket()) {
            int timeout = 2000;
            LOG.info("test db connect, code: {}, hostName: {}, port: {}",
                    dataSource.getCode(), hostName, port);
            socket.connect(socketAddress, timeout);
        } catch (IOException e) {
            LOG.info(e.getMessage(), e);
            return Result.failureResult("测试连接失败，网络不通: " + e.getMessage());
        }*/

        try {
            DataSourceInfo sourceInfo = connectorService.testConnection(dataConnector);
            return Result.successDataResult(sourceInfo);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            return Result.failureResult("测试连接失败: " + e.getMessage());
        }
    }

    @RequestMapping("/connector/saveConnector")
    @ResponseBody
    public Result<Void> saveConnector(DataConnector connector) {
        try {
            connector.setGmtCreated(Instant.now());
            connector.setGmtModified(Instant.now());

            if (connector.getId() == null) {
                connector.setCreater("jobserver");
                connector.setModifier("jobserver");
                connector.setPassword(AESUtils.encrypt(connector.getPassword()));
                connectorService.insertEntity(connector);
            } else {
                DataConnector old = connectorService.getEntity(connector.getId());
                old.setName(connector.getName());
                old.setUsername(connector.getUsername());
                old.setPassword(AESUtils.encrypt(connector.getPassword()));
                old.setJdbcUrl(connector.getJdbcUrl());
                connectorService.updateEntity(old);
            }
            return Result.successResult();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @RequestMapping("/connector/deleteConnector")
    @ResponseBody
    public Result<Void> deleteConnector(Long connectorId) {
        try {
            DataConnector connector = connectorService.getEntity(connectorId);
            connectorService.deleteEntity(connector);
            return Result.successResult();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }
}
