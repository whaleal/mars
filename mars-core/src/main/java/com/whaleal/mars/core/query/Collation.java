/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.core.query;

import com.mongodb.client.model.Collation.Builder;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.icefrog.core.convert.Converter2;
import org.bson.Document;

import java.util.Locale;
import java.util.Optional;

/**
 * @see com.mongodb.client.model.Collation
 *
 *
 * please use com.mongodb.client.model.Collation at first
 *
 * Central abstraction for MongoDB collation support. <br />
 * Allows fluent creation of a collation {@link Document} that can be used for creating collections & indexes as well as
 * querying data.
 * <p/>
 * <strong>NOTE:</strong> Please keep in mind that queries will only make use of an index with collation settings if the
 * query itself specifies the same collation.
 */

public class Collation {

    private static final Collation SIMPLE = of("simple");

    private final CollationLocale locale;


    /**
     * contains  caseLevel  caseFirst  strength
     *
     */
    private Optional<ComparisonLevel> strength = Optional.empty();
    private Optional<Boolean> numericOrdering = Optional.empty();
    /**  contains alternate  maxVariable */
    private Optional<Alternate> alternate = Optional.empty();
    private Optional<Boolean> backwards = Optional.empty();
    private Optional<Boolean> normalization = Optional.empty();
    private Optional<String> version = Optional.empty();

    private Collation(CollationLocale locale) {

        Precondition.notNull(locale, "ICULocale must not be null!");
        this.locale = locale;
    }

    /**
     * Create a {@link Collation} using {@literal simple} binary comparison.
     *
     * @return a {@link Collation} for {@literal simple} binary comparison.
     */
    public static Collation simple() {
        return SIMPLE;
    }

    /**
     * Create new {@link Collation} with locale set to {{@link Locale#getLanguage()}} and
     * {@link Locale#getVariant()}.
     *
     * @param locale must not be {@literal null}.
     * @return new instance of {@link Collation}.
     */
    public static Collation of(Locale locale) {

        Precondition.notNull(locale, "Locale must not be null!");

        String format;

        if (StrUtil.hasText(locale.getCountry())) {
            format = String.format("%s_%s", locale.getLanguage(), locale.getCountry());
        } else {
            format = locale.getLanguage();
        }

        return of(CollationLocale.of(format).variant(locale.getVariant()));
    }

    /**
     * Create new {@link Collation} with locale set to the given ICU language.
     *
     * @param language must not be {@literal null}.
     * @return new instance of {@link Collation}.
     */
    public static Collation of(String language) {
        return of(CollationLocale.of(language));
    }

    /**
     * Create new {@link Collation} with locale set to the given {@link CollationLocale}.
     *
     * @param locale must not be {@literal null}.
     * @return new instance of {@link Collation}.
     */
    public static Collation of(CollationLocale locale) {
        return new Collation(locale);
    }

    /**
     * Parse the given {@code collation} string into a {@link Collation}.
     *
     * @param collation the collation to parse. Can be a simple string like {@code en_US} or a
     *                  {@link Document#parse(String) parsable} document like <code>&#123; 'locale' : '?0' &#125;</code> .
     * @return never {@literal null}.
     * @throws IllegalArgumentException if {@literal collation} is null.
     */
    public static Collation parse(String collation) {

        Precondition.notNull(collation, "Collation must not be null!");

        return StrUtil.trimLeadingWhitespace(collation).startsWith("{") ? from(Document.parse(collation))
                : of(collation);
    }

    /**
     * Create new {@link Collation} from values in {@link Document}.
     *
     * @param source must not be {@literal null}.
     * @return new instance of {@link Collation}.
     * @see <a href="https://docs.mongodb.com/manual/reference/collation/#collation-document">MongoDB Reference -
     * Collation Entity</a>
     */
    public static Collation from(Document source) {

        Precondition.notNull(source, "Source must not be null!");

        Collation collation = Collation.of(source.getString("locale"));
        if (source.containsKey("strength")) {
            collation = collation.strength(source.getInteger("strength"));
        }
        if (source.containsKey("caseLevel")) {
            collation = collation.caseLevel(source.getBoolean("caseLevel"));
        }
        if (source.containsKey("caseFirst")) {
            collation = collation.caseFirst(source.getString("caseFirst"));
        }
        if (source.containsKey("numericOrdering")) {
            collation = collation.numericOrdering(source.getBoolean("numericOrdering"));
        }
        if (source.containsKey("alternate")) {
            collation = collation.alternate(source.getString("alternate"));
        }
        if (source.containsKey("maxVariable")) {
            collation = collation.maxVariable(source.getString("maxVariable"));
        }
        if (source.containsKey("backwards")) {
            collation = collation.backwards(source.getBoolean("backwards"));
        }
        if (source.containsKey("normalization")) {
            collation = collation.normalization(source.getBoolean("normalization"));
        }
        if (source.containsKey("version")) {
            collation.version = Optional.of(source.get("version").toString());
        }
        return collation;
    }

    private static Converter2<Collation, Document> toMongoDocumentConverter() {

        return source -> {

            Document document = new Document();
            document.append("locale", source.locale.asString());

            source.strength.ifPresent(strength -> {

                document.append("strength", strength.getLevel());

                strength.getCaseLevel().ifPresent(it -> document.append("caseLevel", it));
                strength.getCaseFirst().ifPresent(it -> document.append("caseFirst", it.state));
            });

            source.numericOrdering.ifPresent(val -> document.append("numericOrdering", val));
            source.alternate.ifPresent(it -> {

                document.append("alternate", it.alternate);
                it.maxVariable.ifPresent(maxVariable -> document.append("maxVariable", maxVariable));
            });

            source.backwards.ifPresent(it -> document.append("backwards", it));
            source.normalization.ifPresent(it -> document.append("normalization", it));
            source.version.ifPresent(it -> document.append("version", it));

            return document;
        };
    }


    private static Converter2<Collation, com.mongodb.client.model.Collation> toMongoCollationConverter() {

        return source -> {

            Builder builder = com.mongodb.client.model.Collation.builder();

            builder.locale(source.locale.asString());

            source.strength.ifPresent(strength -> {

                builder.collationStrength(CollationStrength.fromInt(strength.getLevel()));

                strength.getCaseLevel().ifPresent(builder::caseLevel);
                strength.getCaseFirst().ifPresent(it -> builder.collationCaseFirst(CollationCaseFirst.fromString(it.state)));
            });

            source.numericOrdering.ifPresent(builder::numericOrdering);
            source.alternate.ifPresent(it -> {

                builder.collationAlternate(CollationAlternate.fromString(it.alternate));
                it.maxVariable
                        .ifPresent(maxVariable -> builder.collationMaxVariable(CollationMaxVariable.fromString(maxVariable)));
            });

            source.backwards.ifPresent(builder::backwards);
            source.normalization.ifPresent(builder::normalization);

            return builder.build();
        };
    }

    /**
     * Set the level of comparison to perform.
     *
     * @param strength comparison level.
     * @return new {@link Collation}.
     */
    public Collation strength(int strength) {

        ComparisonLevel current = this.strength.orElseGet(() -> new ICUComparisonLevel(strength));
        return strength(new ICUComparisonLevel(strength, current.getCaseFirst(), current.getCaseLevel()));
    }

    /**
     * Set the level of comparison to perform.
     *
     * @param comparisonLevel must not be {@literal null}.
     * @return new {@link Collation}
     */
    public Collation strength(ComparisonLevel comparisonLevel) {

        Collation newInstance = copy();
        newInstance.strength = Optional.of(comparisonLevel);
        return newInstance;
    }

    /**
     * Set whether to include {@code caseLevel} comparison. <br />
     *
     * @param caseLevel use {@literal true} to enable {@code caseLevel} comparison.
     * @return new {@link Collation}.
     */
    public Collation caseLevel(boolean caseLevel) {

        ComparisonLevel strengthValue = strength.orElseGet(ComparisonLevel::primary);
        return strength(
                new ICUComparisonLevel(strengthValue.getLevel(), strengthValue.getCaseFirst(), Optional.of(caseLevel)));
    }

    /**
     * Set the flag that determines sort order of case differences during tertiary level comparisons.
     *
     * @param caseFirst must not be {@literal null}.
     * @return new instance of {@link Collation}.
     */
    public Collation caseFirst(String caseFirst) {
        return caseFirst(new CaseFirst(caseFirst));
    }

    /**
     * Set the flag that determines sort order of case differences during tertiary level comparisons.
     *
     * @param sort must not be {@literal null}.
     * @return new instance of {@link Collation}.
     */
    public Collation caseFirst(CaseFirst sort) {

        ComparisonLevel strengthValue = strength.orElseGet(ComparisonLevel::tertiary);
        return strength(new ICUComparisonLevel(strengthValue.getLevel(), Optional.of(sort), strengthValue.getCaseLevel()));
    }

    /**
     * Treat numeric strings as numbers for comparison.
     *
     * @return new {@link Collation}.
     */
    public Collation numericOrderingEnabled() {
        return numericOrdering(true);
    }

    /**
     * Treat numeric strings as string for comparison.
     *
     * @return new {@link Collation}.
     */
    public Collation numericOrderingDisabled() {
        return numericOrdering(false);
    }

    /**
     * Set the flag that determines whether to compare numeric strings as numbers or as strings.
     *
     * @return new {@link Collation}.
     */
    public Collation numericOrdering(boolean flag) {

        Collation newInstance = copy();
        newInstance.numericOrdering = Optional.of(flag);
        return newInstance;
    }

    /**
     * Set the Projection that determines whether collation should consider whitespace and punctuation as base characters for
     * purposes of comparison.
     *
     * @param alternate must not be {@literal null}.
     * @return new {@link Collation}.
     */
    public Collation alternate(String alternate) {

        Alternate instance = this.alternate.orElseGet(() -> new Alternate(alternate, Optional.empty()));
        return alternate(new Alternate(alternate, instance.maxVariable));
    }

    /**
     * Set the Projection that determines whether collation should consider whitespace and punctuation as base characters for
     * purposes of comparison.
     *
     * @param alternate must not be {@literal null}.
     * @return new {@link Collation}.
     */
    public Collation alternate(Alternate alternate) {

        Collation newInstance = copy();
        newInstance.alternate = Optional.of(alternate);
        return newInstance;
    }

    /**
     * Sort string with diacritics sort from back of the string.
     *
     * @return new {@link Collation}.
     */
    public Collation backwardDiacriticSort() {
        return backwards(true);
    }

    /**
     * Do not sort string with diacritics sort from back of the string.
     *
     * @return new {@link Collation}.
     */
    public Collation forwardDiacriticSort() {
        return backwards(false);
    }

    /**
     * Set the flag that determines whether strings with diacritics sort from back of the string.
     *
     * @param backwards must not be {@literal null}.
     * @return new {@link Collation}.
     */
    public Collation backwards(boolean backwards) {

        Collation newInstance = copy();
        newInstance.backwards = Optional.of(backwards);
        return newInstance;
    }

    /**
     * Enable text normalization.
     *
     * @return new {@link Collation}.
     */
    public Collation normalizationEnabled() {
        return normalization(true);
    }

    /**
     * Disable text normalization.
     *
     * @return new {@link Collation}.
     */
    public Collation normalizationDisabled() {
        return normalization(false);
    }

    /**
     * Set the flag that determines whether to check if text require normalization and to perform normalization.
     *
     * @param normalization must not be {@literal null}.
     * @return new {@link Collation}.
     */
    public Collation normalization(boolean normalization) {

        Collation newInstance = copy();
        newInstance.normalization = Optional.of(normalization);
        return newInstance;
    }

    /**
     * Set the field that determines up to which characters are considered ignorable when alternate is {@code shifted}.
     *
     * @param maxVariable must not be {@literal null}.
     * @return new {@link Collation}.
     */
    public Collation maxVariable(String maxVariable) {

        Alternate alternateValue = alternate.orElseGet(Alternate::shifted);
        return alternate(new AlternateWithMaxVariable(alternateValue.alternate, maxVariable));
    }

    /**
     * Get the {@link Document} representation of the {@link Collation}.
     *
     * @return the native MongoDB {@link Document} representation of the {@link Collation}.
     */
    public Document toDocument() {
        return map(toMongoDocumentConverter());
    }

    /**
     * Get the {@link com.mongodb.client.model.Collation} representation of the {@link Collation}.
     *
     * @return he native MongoDB representation of the {@link Collation}.
     */
    public com.mongodb.client.model.Collation toMongoCollation() {
        return map(toMongoCollationConverter());
    }

    /**
     * Transform {@code this} {@link Collation} by applying a {@link Converter2}.
     *
     * @param mapper must not be {@literal null}.
     * @param <R>
     * @return the converted result.
     */
    public <R> R map( Converter2<? super Collation, ? extends R> mapper) {
        return mapper.convert(this);
    }

    @Override
    public String toString() {
        return toDocument().toJson();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Collation that = (Collation) o;
        return this.toDocument().equals(that.toDocument());
    }

    @Override
    public int hashCode() {
        return toDocument().hashCode();
    }

    private Collation copy() {

        Collation collation = new Collation(locale);
        collation.strength = this.strength;
        collation.normalization = this.normalization;
        collation.numericOrdering = this.numericOrdering;
        collation.alternate = this.alternate;
        collation.backwards = this.backwards;
        return collation;
    }

    /**
     * Simple comparison levels.
     */
    enum ComparisonLevels implements ComparisonLevel {

        QUATERNARY(4), IDENTICAL(5);

        private final int level;

        ComparisonLevels(int level) {
            this.level = level;
        }

        @Override
        public int getLevel() {
            return level;
        }
    }

    /**
     * Abstraction for the ICU Comparison Levels.
     */
    public interface ComparisonLevel {

        /**
         * Primary level of comparison. Collation performs comparisons of the base characters only, ignoring other
         * differences such as diacritics and case. <br />
         * The {@code caseLevel} can be set via {@link PrimaryICUComparisonLevel#includeCase()} and
         * {@link PrimaryICUComparisonLevel#excludeCase()}.
         *
         * @return new {@link SecondaryICUComparisonLevel}.
         */
        static PrimaryICUComparisonLevel primary() {
            return PrimaryICUComparisonLevel.DEFAULT;
        }

        /**
         * Secondary level of comparison. Collation performs comparisons up to secondary differences, such as
         * diacritics.<br />
         * The {@code caseLevel} can be set via {@link SecondaryICUComparisonLevel#includeCase()} and
         * {@link SecondaryICUComparisonLevel#excludeCase()}.
         *
         * @return new {@link SecondaryICUComparisonLevel}.
         */
        static SecondaryICUComparisonLevel secondary() {
            return SecondaryICUComparisonLevel.DEFAULT;
        }

        /**
         * Tertiary level of comparison. Collation performs comparisons up to tertiary differences, such as case and letter
         * variants. <br />
         * The {@code caseLevel} cannot be set for {@link ICUComparisonLevel} above {@code secondary}.
         *
         * @return new {@link ICUComparisonLevel}.
         */
        static TertiaryICUComparisonLevel tertiary() {
            return TertiaryICUComparisonLevel.DEFAULT;
        }

        /**
         * Quaternary Level. Limited for specific use case to consider punctuation. <br />
         * The {@code caseLevel} cannot be set for {@link ICUComparisonLevel} above {@code secondary}.
         *
         * @return new {@link ComparisonLevel}.
         */
        static ComparisonLevel quaternary() {
            return ComparisonLevels.QUATERNARY;
        }

        /**
         * Identical Level. Limited for specific use case of tie breaker. <br />
         * The {@code caseLevel} cannot be set for {@link ICUComparisonLevel} above {@code secondary}.
         *
         * @return new {@link ComparisonLevel}.
         */
        static ComparisonLevel identical() {
            return ComparisonLevels.IDENTICAL;
        }

        /**
         * @return collation strength, {@literal 1} for primary, {@literal 2} for secondary and so on.
         */
        int getLevel();

        default Optional<CaseFirst> getCaseFirst() {
            return Optional.empty();
        }

        default Optional<Boolean> getCaseLevel() {
            return Optional.empty();
        }
    }

    /**
     * Abstraction for the ICU Comparison Levels.
     */
    static class ICUComparisonLevel implements ComparisonLevel {

        private final int level;
        private final Optional<CaseFirst> caseFirst;
        private final Optional<Boolean> caseLevel;

        ICUComparisonLevel(int level) {
            this(level, Optional.empty(), Optional.empty());
        }

        ICUComparisonLevel(int level, Optional<CaseFirst> caseFirst, Optional<Boolean> caseLevel) {
            this.level = level;
            this.caseFirst = caseFirst;
            this.caseLevel = caseLevel;
        }

        public int getLevel() {
            return this.level;
        }

        public Optional<CaseFirst> getCaseFirst() {
            return this.caseFirst;
        }

        public Optional<Boolean> getCaseLevel() {
            return this.caseLevel;
        }
    }

    /**
     * Primary-strength {@link ICUComparisonLevel}.
     */
    public static class PrimaryICUComparisonLevel extends ICUComparisonLevel {

        static final PrimaryICUComparisonLevel DEFAULT = new PrimaryICUComparisonLevel();
        static final PrimaryICUComparisonLevel WITH_CASE_LEVEL = new PrimaryICUComparisonLevel(true);
        static final PrimaryICUComparisonLevel WITHOUT_CASE_LEVEL = new PrimaryICUComparisonLevel(false);

        private PrimaryICUComparisonLevel() {
            super(1);
        }

        private PrimaryICUComparisonLevel(boolean caseLevel) {
            super(1, Optional.empty(), Optional.of(caseLevel));
        }

        /**
         * Include case comparison.
         *
         * @return new {@link ICUComparisonLevel}
         */
        public ComparisonLevel includeCase() {
            return WITH_CASE_LEVEL;
        }

        /**
         * Exclude case comparison.
         *
         * @return new {@link ICUComparisonLevel}
         */
        public ComparisonLevel excludeCase() {
            return WITHOUT_CASE_LEVEL;
        }
    }

    /**
     * Secondary-strength {@link ICUComparisonLevel}.
     */
    public static class SecondaryICUComparisonLevel extends ICUComparisonLevel {

        static final SecondaryICUComparisonLevel DEFAULT = new SecondaryICUComparisonLevel();
        static final SecondaryICUComparisonLevel WITH_CASE_LEVEL = new SecondaryICUComparisonLevel(true);
        static final SecondaryICUComparisonLevel WITHOUT_CASE_LEVEL = new SecondaryICUComparisonLevel(false);

        private SecondaryICUComparisonLevel() {
            super(2);
        }

        private SecondaryICUComparisonLevel(boolean caseLevel) {
            super(2, Optional.empty(), Optional.of(caseLevel));
        }

        /**
         * Include case comparison.
         *
         * @return new {@link SecondaryICUComparisonLevel}
         */
        public ComparisonLevel includeCase() {
            return WITH_CASE_LEVEL;
        }

        /**
         * Exclude case comparison.
         *
         * @return new {@link SecondaryICUComparisonLevel}
         */
        public ComparisonLevel excludeCase() {
            return WITHOUT_CASE_LEVEL;
        }
    }

    /**
     * Tertiary-strength {@link ICUComparisonLevel}.
     */
    public static class TertiaryICUComparisonLevel extends ICUComparisonLevel {

        static final TertiaryICUComparisonLevel DEFAULT = new TertiaryICUComparisonLevel();

        private TertiaryICUComparisonLevel() {
            super(3);
        }

        private TertiaryICUComparisonLevel(CaseFirst caseFirst) {
            super(3, Optional.of(caseFirst), Optional.empty());
        }

        /**
         * Set the flag that determines sort order of case differences.
         *
         * @param caseFirst must not be {@literal null}.
         * @return new {@link ICUComparisonLevel}
         */
        public ComparisonLevel caseFirst(CaseFirst caseFirst) {

            Precondition.notNull(caseFirst, "CaseFirst must not be null!");
            return new TertiaryICUComparisonLevel(caseFirst);
        }
    }


    public static class CaseFirst {

        private static final CaseFirst UPPER = new CaseFirst("upper");
        private static final CaseFirst LOWER = new CaseFirst("lower");
        private static final CaseFirst OFF = new CaseFirst("off");

        private final String state;

        private CaseFirst(String state) {
            this.state = state;
        }

        /**
         * Sort uppercase before lowercase.
         *
         * @return new {@link CaseFirst}.
         */
        public static CaseFirst upper() {
            return UPPER;
        }

        /**
         * Sort lowercase before uppercase.
         *
         * @return new {@link CaseFirst}.
         */
        public static CaseFirst lower() {
            return LOWER;
        }

        /**
         * Use the default.
         *
         * @return new {@link CaseFirst}.
         */
        public static CaseFirst off() {
            return OFF;
        }
    }


    public static class Alternate {

        private static final Alternate NON_IGNORABLE = new Alternate("non-ignorable", Optional.empty());

        final String alternate;
        final Optional<String> maxVariable;

        Alternate(String alternate, Optional<String> maxVariable) {
            this.alternate = alternate;
            this.maxVariable = maxVariable;
        }

        /**
         * Consider Whitespace and punctuation as base characters.
         *
         * @return new {@link Alternate}.
         */
        public static Alternate nonIgnorable() {
            return NON_IGNORABLE;
        }

        /**
         * Whitespace and punctuation are <strong>not</strong> considered base characters and are only distinguished at
         * strength. <br />
         * <strong>NOTE:</strong> Only works for {@link ICUComparisonLevel} above {@link ComparisonLevel#tertiary()}.
         *
         * @return new {@link AlternateWithMaxVariable}.
         */
        public static AlternateWithMaxVariable shifted() {
            return AlternateWithMaxVariable.DEFAULT;
        }
    }


    public static class AlternateWithMaxVariable extends Alternate {

        static final AlternateWithMaxVariable DEFAULT = new AlternateWithMaxVariable("shifted");
        static final Alternate SHIFTED_PUNCT = new AlternateWithMaxVariable("shifted", "punct");
        static final Alternate SHIFTED_SPACE = new AlternateWithMaxVariable("shifted", "space");

        private AlternateWithMaxVariable(String alternate) {
            super(alternate, Optional.empty());
        }

        private AlternateWithMaxVariable(String alternate, String maxVariable) {
            super(alternate, Optional.of(maxVariable));
        }

        /**
         * Consider both whitespaces and punctuation as ignorable.
         *
         * @return new {@link AlternateWithMaxVariable}.
         */
        public Alternate punct() {
            return SHIFTED_PUNCT;
        }

        /**
         * Only consider whitespaces as ignorable.
         *
         * @return new {@link AlternateWithMaxVariable}.
         */
        public Alternate space() {
            return SHIFTED_SPACE;
        }
    }

    /**
     * ICU locale abstraction for usage with MongoDB {@link Collation}.
     *
     * @see <a href="http://site.icu-project.org">ICU - International Components for Unicode</a>
     */
    public static class CollationLocale {

        private final String language;
        private final Optional<String> variant;

        private CollationLocale(String language, Optional<String> variant) {

            this.language = language;
            this.variant = variant;
        }

        /**
         * Create new {@link CollationLocale} for given language.
         *
         * @param language must not be {@literal null}.
         * @return new instance of {@link CollationLocale}.
         */
        public static CollationLocale of(String language) {

            Precondition.notNull(language, "Code must not be null!");
            return new CollationLocale(language, Optional.empty());
        }

        /**
         * Define language variant.
         *
         * @param variant must not be {@literal null}.
         * @return new {@link CollationLocale}.
         */
        public CollationLocale variant(String variant) {

            Precondition.notNull(variant, "Variant must not be null!");
            return new CollationLocale(language, Optional.of(variant));
        }

        /**
         * Get the string representation.
         *
         * @return the collation {@link String} in Mongo ICU format.
         */
        public String asString() {

            StringBuilder sb = new StringBuilder(language);

            variant.filter(it -> !it.isEmpty()).ifPresent(val -> {

                // Mongo requires variant rendered as ICU keyword (@key=value;key=value…)
                sb.append("@collation=").append(val);
            });

            return sb.toString();
        }
    }
}
