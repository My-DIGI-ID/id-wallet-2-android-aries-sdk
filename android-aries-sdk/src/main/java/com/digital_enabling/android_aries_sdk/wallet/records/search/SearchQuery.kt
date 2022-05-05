package com.digital_enabling.android_aries_sdk.wallet.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Wallet query builder
 */
class SearchQuery {

    companion object {
        /**
         * Empty query.
         */
        var empty: ISearchQuery = SearchExpression.EmptyExpression()

        /**
         * Combine the specified queries with AND operator.
         * @param queries Queries.
         * @return The and query.
         */
        fun and(queries: List<ISearchQuery>): ISearchQuery = SearchExpression.AndSubquery(queries)
        /**
         * Combine the specified queries with OR operator.
         * @param queries Queries.
         * @return The or query.
         */
        fun or(queries: List<ISearchQuery>): ISearchQuery = SearchExpression.OrSubquery(queries)
        /**
         * Apply NOT operator to the query.
         * @param queries Query.
         * @return The query applied with not operator.
         */
        fun not(queries: ISearchQuery): ISearchQuery = SearchExpression.NotSubquery(queries)
        /**
         * EQUAL expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with equal operator.
         */
        fun equal(name: String, value: String): ISearchQuery = SearchExpression.EqSubquery(name, value)

        /**
         * NOT EQUAL expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with not equal operator.
         */
        fun notEqual(name: String, value: String): ISearchQuery = SearchExpression.NotEqSubquery(name, value)
        /**
         * NOT EQUAL expression for DateTime.
         * @param name  The name.
         * @param value The DateTime value.
         * @return The query applied with not equal operator.
         */
        fun notEqual(name: String, value: LocalDateTime): ISearchQuery = SearchExpression.NotEqSubquery(name, value.toEpochSecond(ZoneOffset.UTC).toString())

        /**
         * IN expression, search values found inside the collection.
         * @param name  The name.
         * @param value The value.
         * @return The query found inside the collection.
         */
        fun inSubquery(name: String, values: List<String>): ISearchQuery = SearchExpression.InSubquery(name, values)

        /**
         * LESS THAN expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with not less than operator.
         */
        fun less(name: String, value: String): ISearchQuery = SearchExpression.LtSubquery(name, value)
        /**
         * LESS THAN expression for DateTime.
         * @param name  The name.
         * @param value The DateTime value.
         * @return The query applied with not less than operator.
         */
        fun less(name: String, value: LocalDateTime): ISearchQuery = SearchExpression.LtSubquery(name, value.toEpochSecond(ZoneOffset.UTC).toString())

        /**
         * LESS THAN OR EQUAL expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with not less than or equal operator.
         */
        fun lessOrEqual(name: String, value: String): ISearchQuery = SearchExpression.LteSubquery(name, value)
        /**
         * LESS THAN OR EQUAL expression for DateTime.
         * @param name  The name.
         * @param value The DateTime value.
         * @return The query applied with not less than or equal operator.
         */
        fun lessOrEqual(name: String, value: LocalDateTime): ISearchQuery = SearchExpression.LteSubquery(name, value.toEpochSecond(ZoneOffset.UTC).toString())

        /**
         * GREATER THAN expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with not greater than operator.
         */
        fun greater(name: String, value: String): ISearchQuery = SearchExpression.GtSubquery(name, value)
        /**
         * GREATER THAN expression for DateTime.
         * @param name  The name.
         * @param value The DateTime value.
         * @return The query applied with not greater than operator.
         */
        fun greater(name: String, value: LocalDateTime): ISearchQuery = SearchExpression.GtSubquery(name, value.toEpochSecond(ZoneOffset.UTC).toString())

        /**
         * GREATER THAN OR EQUAL expression.
         * @param name  The name.
         * @param value The value.
         * @return The query applied with not greater than or equal operator.
         */
        fun greaterOrEqual(name: String, value: String): ISearchQuery = SearchExpression.GteSubquery(name, value)
        /**
         * GREATER THAN OR EQUAL expression for DateTime.
         * @param name  The name.
         * @param value The DateTime value.
         * @return The query applied with not greater than or equal operator.
         */
        fun greaterOrEqual(name: String, value: LocalDateTime): ISearchQuery = SearchExpression.GteSubquery(name, value.toEpochSecond(ZoneOffset.UTC).toString())

        /**
         * LIKE expression, checks if substring is contained anywhere in the value
         * @param name  The name.
         * @param value The value.
         * @return The query as substring, which was found anywhere in the given query.
         */
        fun contains(name: String, value: String): ISearchQuery = SearchExpression.LikeSubquery(name, "%$value%")

        /**
         * LIKE expression, checks if substring is contained at the beginning of the value
         * @param name  The name.
         * @param value The value.
         * @return The query as substring, which was found at the beginning of the given query.
         */
        fun startsWith(name: String, value: String): ISearchQuery = SearchExpression.LikeSubquery(name, "%$value%")

        /**
         * LIKE expression, checks if substring is contained at the end of the value
         * @param name  The name.
         * @param value The value.
         * @return The query as substring, which was found at the end of the given query.
         */
        fun endsWith(name: String, value: String): ISearchQuery = SearchExpression.LikeSubquery(name, "%$value%")

    }
}