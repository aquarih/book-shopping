package myssm.basedao;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO<T> {
    protected Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;

    private Class clazz;

    public BaseDAO() {
        Type genericType = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        Type actualType = actualTypeArguments[0];

        try {
            clazz = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO constructor出錯，可能是沒有指定<>中的類型");
        }
    }

    protected Connection getConnection() {
        return ConnUtil.getConn();
    }

    protected void closeResource(Connection conn, PreparedStatement ps, ResultSet rs){
//        try {
//            if(conn != null && !conn.isClosed()){
//                conn.close();
//            }
//
//            if(ps != null){
//                ps.close();
//            }
//
//            if(rs != null){
//                rs.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private void setParams(PreparedStatement ps, Object...args) throws SQLException {
        if(args != null && args.length > 0){
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1, args[i]);
            }
        }
    }

    //執行更新，返回影響行數
    protected int executeUpdate(String sql , Object...args) {
        boolean insertFlag = false ;
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");

        conn = getConnection();
        try {
            if (insertFlag) {
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(sql);
            }
            setParams(ps, args);
            int count = ps.executeUpdate();

            if (insertFlag) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return ((Long) rs.getLong(1)).intValue();
                }
            }

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeUpdate出錯");
        }
    }

    public static boolean isNotMyType(String typeName){
        return "java.lang.Integer".equals(typeName)
                || "java.lang.String".equals(typeName)
                || "java.sql.Date".equals(typeName)
                || "java.util.Date".equals(typeName)
                || "java.lang.Double".equals(typeName)
                || "java.time.LocalDateTime".equals(typeName);
    }

    public static boolean isMyType(String typeName) {
        return !isNotMyType(typeName);
    }

    //通過映射給obj的property屬性賦予propertyValue值
    private void setValue(Object obj,  String property, Object propertyValue) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class clazz = obj.getClass();

        //獲取property這個字符串對應的屬性名，比如"fid"去找obj对象中的fid属性
        Field field = clazz.getDeclaredField(property);
        if(field != null){
            //獲取當前字段的類型名稱
            String typeName = field.getType().getName();
            //判斷如果是自定義類型，則需要調用這個自定義類的帶一個參數之構造方法，創建出這個自定義的實例對象，然後將實例對象賦值給這個屬性
            if(isMyType(typeName)) {
                //假設typeName是"java.qqzone.pojo.UserBasic"
                Class<?> typeNameClass = Class.forName(typeName);
                Constructor constructor = typeNameClass.getDeclaredConstructor(Integer.class);
                propertyValue = constructor.newInstance(propertyValue);
            }
            field.setAccessible(true);
            field.set(obj, propertyValue);
        }
    }

    //執行複雜查询，返回例如統計結果
    protected Object[] executeComplexQuery(String sql, Object...args) {
        conn = getConnection() ;
        try {
            ps = conn.prepareStatement(sql);
            setParams(ps, args);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            //獲取結果集的列數
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];

            if (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeComplexQuery出錯");
        }
        return null;
    }

    //執行查詢，返回單個實體對象
    protected T load(String sql , Object...args) {
        conn = getConnection() ;
        try {
            ps = conn.prepareStatement(sql);
            setParams(ps, args);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            //獲取結果集的列數
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T entity = (T) clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    setValue(entity, columnName, columnValue);
                }
                return entity;
            }
        } catch (Exception e) {
            throw new DAOException("BaseDAO load出錯");
        }
        return null ;
    }


    //執行查詢，返回List
    protected List<T> executeQuery(String sql , Object... params) {
        List<T> list = new ArrayList<>();

        conn = getConnection() ;
        try {
            ps = conn.prepareStatement(sql);
            setParams(ps, params);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                T entity = (T) clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);            //fid   fname   price
                    Object columnValue = rs.getObject(i + 1);     //33    苹果      5
                    setValue(entity, columnLabel, columnValue);
                }
                list.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeQuery出錯");
        }
        return list ;
    }
}
