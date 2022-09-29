package com.whaleal.mars.core.aggregation.stages;

import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.core.aggregation.expressions.TimeUnit;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.domain.ISort;

import java.util.List;

/**
 * Performs operations on a specified span of documents in a collection, known as a window, and returns the results based on the chosen
 * window operator.
 */
public class SetWindowFields extends Stage {
    private Expression partition;
    private ISort sorts;
    private Output[] outputs;

    protected SetWindowFields() {
        super("$setWindowFields");
    }

    public static SetWindowFields setWindowFields() {
        return new SetWindowFields();
    }

    public SetWindowFields output(Output... outputs) {
        this.outputs = outputs;
        return this;
    }

    public Output[] outputs() {
        return outputs;
    }

    @Nullable
    public Expression partition() {
        return partition;
    }

    /**
     * Defines the expression to use to partition the data.
     *
     * @param partition the expression
     * @return this
     */
    public SetWindowFields partitionBy(Expression partition) {
        this.partition = partition;
        return this;
    }

    public SetWindowFields sortBy(ISort sorts) {
        this.sorts = sorts;
        return this;
    }

    /**
     * @return the sort values
     */
    @Nullable
    public ISort sorts() {
        return sorts;
    }

    public static class Output {
        private final String name;
        private Expression operator;
        private Window window;

        private Output(String name) {
            this.name = name;
        }

        public static Output output(String name) {
            return new Output(name);
        }

        /**
         * @return the name
         */

        public String name() {
            return name;
        }

        /**
         * @return the operator
         */
        @Nullable

        public Expression operator() {
            return operator;
        }

        public Output operator(Expression operator) {
            this.operator = operator;
            return this;
        }

        public Window window() {
            window = new Window(this);
            return window;
        }

        /**
         * @return the window
         */
        @Nullable

        public Window windowDef() {
            return window;
        }
    }

    public static class Window {
        private final Output output;
        private List<Object> documents;
        private List<Object> range;
        private TimeUnit unit;

        private Window(Output output) {
            this.output = output;
        }

        public Output documents(Object lower, Object upper) {
            documents = ListUtil.of(lower, upper);
            return output;
        }

        /**
         * @return the documents
         */
        @Nullable
        public List<Object> documents() {
            return documents;
        }

        public Output range(Object lower, Object upper, TimeUnit unit) {
            range = ListUtil.of(lower, upper);
            this.unit = unit;
            return output;
        }

        public Output range(Object lower, Object upper) {
            range = ListUtil.of(lower, upper);
            return output;
        }

        /**
         * @return the range
         */
        @Nullable

        public List<Object> range() {
            return range;
        }

        /**
         * @return the unit
         */
        @Nullable
        public TimeUnit unit() {
            return unit;
        }
    }
}
