package org.cratedb.action.collect;

import org.apache.lucene.index.AtomicReaderContext;
import org.cratedb.DataType;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;

public class FloatColumnReference extends FieldCacheExpression<IndexNumericFieldData, Float> {

    DoubleValues values;

    public FloatColumnReference(String columnName) {
        super(columnName);
    }

    @Override
    public Float evaluate() {
        Float value = ((Double)values.getValue(docId)).floatValue();
        if (value == 0 && !values.hasValue(docId)) {
            return null;
        }
        return value;
    }

    @Override
    public void setNextReader(AtomicReaderContext context) {
        super.setNextReader(context);
        values = indexFieldData.load(context).getDoubleValues();
    }

    @Override
    public DataType returnType(){
        return DataType.FLOAT;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof FloatColumnReference))
            return false;
        return columnName.equals(((FloatColumnReference) obj).columnName);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }
}
