package org.apache.kylin.dict;

import java.nio.ByteBuffer;

import org.apache.kylin.common.util.BytesUtil;
import org.apache.kylin.dimension.DimensionEncoding;
import org.apache.kylin.metadata.filter.IFilterCodeSystem;

/**
 * A simple code system where all values are dictionary IDs (fixed length bytes) encoded as ISO-8859-1 strings.
 *
 * @author yangli9
 */
public class DictCodeSystem implements IFilterCodeSystem<String> {

    public static final DictCodeSystem INSTANCE = new DictCodeSystem();

    private DictCodeSystem() {
        // singleton
    }

    @Override
    public boolean isNull(String value) {
        if (value == null)
            return true;

        String v = value;
        for (int i = 0, n = v.length(); i < n; i++) {
            if ((byte) v.charAt(i) != DimensionEncoding.NULL)
                return false;
        }
        return true;
    }

    @Override
    public int compare(String tupleValue, String constValue) {
        return tupleValue.compareTo(constValue);
    }

    //TODO: should use ISO-8859-1 rather than UTF8
    @Override
    public void serialize(String value, ByteBuffer buffer) {
        BytesUtil.writeUTFString(value, buffer);
    }

    @Override
    public String deserialize(ByteBuffer buffer) {
        return BytesUtil.readUTFString(buffer);
    }

}
