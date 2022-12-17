package io.github.melin.spark.jobserver.support;

import io.github.melin.spark.jobserver.core.entity.Cluster;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class KerberosLogin {

    private static final Logger logger = LoggerFactory.getLogger(KerberosLogin.class);

    public static final String DEFAULT_KEYTAB_FILE_NAME = "jobserver.keytab";

    /**
     * 登录到kerberos认证
     * @param keytabFile    keytab 文件路径
     * @param kerberosUser  kerberos用户
     * @return
     */
    public UserGroupInformation loginToKerberos(String keytabFile, String kerberConfFile,
                                                String kerberosUser, Configuration conf) throws IOException {
        if (StringUtils.isEmpty(kerberConfFile) || StringUtils.isEmpty(keytabFile)) {
            throw new RuntimeException("can not get krb5conf or keytab file");
        }

        //https://stackoverflow.com/questions/34616676/should-i-call-ugi-checktgtandreloginfromkeytab-before-every-action-on-hadoop
        UserGroupInformation connectUgi = UserGroupInformation.getCurrentUser();
        if (!connectUgi.isFromKeytab()) {
            System.setProperty("java.security.krb5.conf", kerberConfFile);
            UserGroupInformation.setConfiguration(conf);
            UserGroupInformation.loginUserFromKeytab(kerberosUser, keytabFile);

            connectUgi = UserGroupInformation.getCurrentUser();
        }

        connectUgi.checkTGTAndReloginFromKeytab();

        return connectUgi;
    }

    /**
     * 下载配置keytab 文件，返回下载本地keytab 文件路径
     */
    public void downloadKeytabFile(Cluster cluster, String dir) throws IOException {
        if (cluster != null) {
            logger.info("download keytab file: {}", cluster.getCode());
            String name = cluster.getKerberosFileName();

            // 如果直接上传zip 文件需要解压获取 keytab文件，也可以直接上传keytab 文件
            if (StringUtils.endsWithIgnoreCase(name, ".keytab")) {
                String configFile = dir + "/" + DEFAULT_KEYTAB_FILE_NAME;
                FileUtils.writeByteArrayToFile(new File(configFile), cluster.getKerberosKeytab());

                FileUtils.write(new File(dir + "/krb5.conf"), cluster.getKerberosConfig(), StandardCharsets.UTF_8);
            } else {
                throw new IllegalArgumentException("keytab 文件名后缀为：.keytab, 当前值为：" + name);
            }
        }
    }
}
