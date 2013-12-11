package org.cratedb.action.collect;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.BytesRef;
import org.cratedb.DataType;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;

public class DoubleColumnReference extends FieldCacheExpression<IndexNumericFieldData, Double> {

    private DoubleValues values;

    public DoubleColumnReference(String columnName) {
        super(columnName);
    }

    @Override
    public Double evaluate() {
        Double value = values.getValue(docId);
        if (value == 0.0 && !values.hasValue(docId)) {
            return null;
        }
        return value;
    }

    @Override
    public void setNextReader(AtomicReaderContext context) {
        super.setNextReader(context);
        values = ((IndexNumericFieldData) indexFieldData).load(context).getDoubleValues();
    }

    @Override
    public DataType returnType(){
        return DataType.DOUBLE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof DoubleColumnReference))
            return false;
        return columnName.equals(((DoubleColumnReference) obj).columnName);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }
}

