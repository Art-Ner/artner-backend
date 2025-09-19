package kr.artner.global.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class VectorType implements UserType<String> {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<String> returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(String x, String y) {
        return x != null ? x.equals(y) : y == null;
    }

    @Override
    public int hashCode(String x) {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    public String nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String value = rs.getString(position);
        return rs.wasNull() ? null : value;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, String value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            // PGobject를 사용해서 vector 타입으로 설정
            try {
                Class<?> pgObjectClass = Class.forName("org.postgresql.util.PGobject");
                Object pgObject = pgObjectClass.getDeclaredConstructor().newInstance();
                pgObjectClass.getMethod("setType", String.class).invoke(pgObject, "vector");
                pgObjectClass.getMethod("setValue", String.class).invoke(pgObject, value);
                st.setObject(index, pgObject);
            } catch (Exception e) {
                // PGobject를 사용할 수 없으면 기본 방식으로 설정
                st.setObject(index, value, Types.OTHER);
            }
        }
    }

    @Override
    public String deepCopy(String value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(String value) {
        return value;
    }

    @Override
    public String assemble(Serializable cached, Object owner) {
        return (String) cached;
    }
}