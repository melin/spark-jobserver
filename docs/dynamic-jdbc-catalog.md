### 动态注册catalog
Connector Admin 配置数据库连接，Code 注册为spark catalog name，例如添加mysql connector，code = mysql_demo，mysql 数据库 examples 包含表：users。spark 作业中访问表：
```sql
    select * from mysql_demo.examples.users
```

### 注意
1. 自动根据表主键信息和表总数据量，推断 partitionColumn, lowerBound, upperBound, numPartitions 参数值，提高并发度。具体参考 [JDBCTableCatalog.scala](..%2Fjobserver-driver%2Fsrc%2Fmain%2Fscala%2Fio%2Fgithub%2Fmelin%2Fspark%2Fjobserver%2Fdriver%2Fcatalog%2FJDBCTableCatalog.scala) 实现，有待完善
