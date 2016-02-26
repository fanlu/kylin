package org.apache.kylin.storage.translate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.kylin.dict.StringBytesConverter;
import org.apache.kylin.dict.TrieDictionaryBuilder;
import org.apache.kylin.dimension.Dictionary;
import org.apache.kylin.metadata.filter.TupleFilter.FilterOperatorEnum;
import org.apache.kylin.metadata.model.ColumnDesc;
import org.apache.kylin.metadata.model.TableDesc;
import org.apache.kylin.metadata.model.TblColRef;
import org.junit.Test;

public class ColumnValueRangeTest {

    @Test
    public void testPreEvaluateWithDict() {
        TblColRef col = mockupTblColRef();
        Dictionary<String> dict = mockupDictionary(col, "CN", "US");

        ColumnValueRange r1 = new ColumnValueRange(col, set("CN", "US", "Other"), FilterOperatorEnum.EQ);
        r1.preEvaluateWithDict(dict);
        assertEquals(set("CN", "US"), r1.getEqualValues());

        // less than rounding
        {
            ColumnValueRange r2 = new ColumnValueRange(col, set("CN"), FilterOperatorEnum.LT);
            r2.preEvaluateWithDict(dict);
            assertEquals(null, r2.getBeginValue());
            assertEquals("CN", r2.getEndValue());

            ColumnValueRange r3 = new ColumnValueRange(col, set("Other"), FilterOperatorEnum.LT);
            r3.preEvaluateWithDict(dict);
            assertEquals(null, r3.getBeginValue());
            assertEquals("CN", r3.getEndValue());

            ColumnValueRange r4 = new ColumnValueRange(col, set("UT"), FilterOperatorEnum.LT);
            r4.preEvaluateWithDict(dict);
            assertEquals(null, r4.getBeginValue());
            assertEquals("US", r4.getEndValue());
        }

        // greater than rounding
        {
            ColumnValueRange r2 = new ColumnValueRange(col, set("CN"), FilterOperatorEnum.GTE);
            r2.preEvaluateWithDict(dict);
            assertEquals("CN", r2.getBeginValue());
            assertEquals(null, r2.getEndValue());

            ColumnValueRange r3 = new ColumnValueRange(col, set("Other"), FilterOperatorEnum.GTE);
            r3.preEvaluateWithDict(dict);
            assertEquals("US", r3.getBeginValue());
            assertEquals(null, r3.getEndValue());

            ColumnValueRange r4 = new ColumnValueRange(col, set("CI"), FilterOperatorEnum.GTE);
            r4.preEvaluateWithDict(dict);
            assertEquals("CN", r4.getBeginValue());
            assertEquals(null, r4.getEndValue());
        }
        
        // ever false check
        {
            ColumnValueRange r2 = new ColumnValueRange(col, set("UT"), FilterOperatorEnum.GTE);
            r2.preEvaluateWithDict(dict);
            assertTrue(r2.satisfyNone());

            ColumnValueRange r3 = new ColumnValueRange(col, set("CM"), FilterOperatorEnum.LT);
            r3.preEvaluateWithDict(dict);
            assertTrue(r3.satisfyNone());
        }
    }

    public static Dictionary<String> mockupDictionary(TblColRef col, String... values) {
        TrieDictionaryBuilder<String> builder = new TrieDictionaryBuilder<String>(new StringBytesConverter());
        for (String v : values) {
            builder.addValue(v);
        }
        return builder.build(0);
    }

    private static Set<String> set(String... values) {
        HashSet<String> list = new HashSet<String>();
        list.addAll(Arrays.asList(values));
        return list;
    }

    public static TblColRef mockupTblColRef() {
        TableDesc t = mockupTableDesc("table_a");
        ColumnDesc c = mockupColumnDesc(t, 1, "col_1", "string");
        return new TblColRef(c);
    }

    private static TableDesc mockupTableDesc(String tableName) {
        TableDesc mockup = new TableDesc();
        mockup.setName(tableName);
        return mockup;
    }

    private static ColumnDesc mockupColumnDesc(TableDesc table, int oneBasedColumnIndex, String name, String datatype) {
        ColumnDesc desc = new ColumnDesc();
        String id = "" + oneBasedColumnIndex;
        desc.setId(id);
        desc.setName(name);
        desc.setDatatype(datatype);
        desc.init(table);
        return desc;
    }
}
