package com.whaleal.mars.core.codecs;

import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.domain.SortType;
import org.bson.Document;
import org.junit.Test;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;

/**
 * @author wh
 */
public class TestSort {


    @Test
    public void test01(){


        Sort  sort = Sort.on().ascending("aa","bb").descending("cc","dd").meta("gg");

        DocumentWriter writer = new DocumentWriter();

        document(writer, ()->{
            for (SortType sortSort : sort.getSorts()) {
                writer.writeName(sortSort.getField());
                sortSort.getDirection().encode(writer);
            }

        });


        Document document = writer.getDocument();

        System.out.println(document);

    }



    @Test
    public void test02(){

        DocumentWriter writer = new DocumentWriter();
        writer.writeStartDocument();


        writer.writeString("abc","abcvalue");

        writer.writeEndDocument();

        Document document = writer.getDocument();
        System.out.println(document);

    }
}
