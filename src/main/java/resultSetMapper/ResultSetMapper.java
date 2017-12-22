package resultSetMapper;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
This class will map resultset to list of object of any class.
 */
public class ResultSetMapper<T> {

    //In objectForT value of field will be set
    private void setValueInObject(Object objectForT, String fieldName, Object objectValue)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = objectForT.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(objectForT, objectValue);
    }
    List<T> getObjectsFromResultSet(ResultSet resultSet, Class clazz) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchFieldException {
        ResultSetMetaData rsMetadata = resultSet.getMetaData();
        List<T> outputList = new ArrayList<>();
        int columnCount = rsMetadata.getColumnCount();
        Field[] fields = clazz.getDeclaredFields();
        while (resultSet.next())
        {
            T object = (T)clazz.newInstance();
            for(int i=0;i<columnCount;i++)
            {
                //i+1 is because index starts at 1 in result set
                Object valueForColumn = resultSet.getObject(i+1);
                String columnName = rsMetadata.getColumnName(i+1);
                String fieldName = getFieldName(columnName, fields);
                setValueInObject(object, fieldName, valueForColumn);
            }
            outputList.add(object);
        }
        return outputList;
    }

    private String getFieldName(String columnName, Field[] fields)
    {
        for(Field field : fields)
        {
            if(field.isAnnotationPresent(Column.class))
            {
                Column column = field.getAnnotation(Column.class);
                if(column.name().equals(columnName))
                {
                    return field.getName();
                }
            }
        }
        throw new IllegalArgumentException("No such column name: " + columnName);
    }

}
