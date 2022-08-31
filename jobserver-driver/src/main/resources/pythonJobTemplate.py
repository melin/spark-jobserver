#-*- coding: UTF-8 -*-

from __future__ import print_function
from __future__ import division
import os,sys,inspect
import threading,signal
from py4j.java_gateway import JavaGateway, java_import, GatewayClient, GatewayParameters
from pyspark import *
from pyspark.sql import *
from pyspark.sql.types import *
from pyspark.sql.session import SparkSession
import traceback

class Config:
    jobInstanceCode = None
    entry_point = None


def info(msg):
    Config.entry_point.sendMsg("info", msg)


def warn(msg):
    Config.entry_point.sendMsg("warn", msg)


def error(msg):
    Config.entry_point.sendMsg("error", msg)


def _get_thread(tid):
    for t in threading.enumerate():
        if t.ident == tid:
            return t
    return None


def pystack():
    stackInfo = []
    for tid, stack in sys._current_frames().items():
        threadInfo = []
        t = _get_thread(tid)
        threadInfo.append("%s tid=%d" % (t.name, tid))
        for filename, lineno, _, line in traceback.extract_stack(stack):
            threadInfo.append("    at %s(%s:%d)" % (line, filename[filename.rfind('/')+1:], lineno))
        stackInfo.append('\n'.join(threadInfo))
    Config.entry_point.setPythonStack('\n\n'.join(stackInfo))


def _pystack(sig, frame):
    pystack()


##PYTHON_TEMPLE##


class CaptureOutput:
    def write(self, message):
        Config.entry_point.sendMsg("stdout", message)

    def flush(self):
        None

if __name__ == "__main__":
    sys.stdout = CaptureOutput()

    gateway_port = int(sys.argv[1])
    Config.jobInstanceCode = sys.argv[2]
    gateway_secret = sys.argv[3]

    gateway = JavaGateway(gateway_parameters=GatewayParameters(port=gateway_port, auth_token=gateway_secret, auto_convert=True))

    java_import(gateway.jvm, "org.apache.spark.SparkConf")
    java_import(gateway.jvm, "org.apache.spark.api.java.*")
    java_import(gateway.jvm, "org.apache.spark.api.python.*")
    java_import(gateway.jvm, "scala.Tuple2")
    java_import(gateway.jvm, "org.apache.spark.SparkContext")
    java_import(gateway.jvm, "org.apache.spark.sql.SQLContext")
    java_import(gateway.jvm, "org.apache.spark.sql.UDFRegistration")
    java_import(gateway.jvm, "org.apache.spark.sql.hive.HiveContext")
    java_import(gateway.jvm, "org.apache.spark.sql.*")
    java_import(gateway.jvm, "org.apache.spark.sql.hive.*")
    java_import(gateway.jvm, "org.apache.spark.sql.api.python.*")
    java_import(gateway.jvm, "org.apache.spark.ml.python.*")
    java_import(gateway.jvm, "org.apache.spark.mllib.api.python.*")

    Config.entry_point = gateway.entry_point

    jsparkSesssion = Config.entry_point.getSparkSession()
    jsparkContext = Config.entry_point.getSparkContext()
    jspark_conf = Config.entry_point.getSparkConf()
    spark_conf = SparkConf(_jconf=jspark_conf)

    pythonExec = os.environ["PYSPARK_PYTHON"]
    pysparkPath = os.environ["PYTHONPATH"]
    sc = SparkContext(gateway=gateway, jsc=jsparkContext, conf=spark_conf)
    sc._conf.set("spark.pyspark.driver.python", pythonExec)
    sc._conf.set("spark.pyspark.python", pythonExec)
    sc._conf.set("spark.executorEnv.PYTHONPATH", pysparkPath)

    signal.signal(signal.SIGUSR1, _pystack)
    sparkSession = SparkSession(sc, jsparkSesssion)
    main(sparkSession)


