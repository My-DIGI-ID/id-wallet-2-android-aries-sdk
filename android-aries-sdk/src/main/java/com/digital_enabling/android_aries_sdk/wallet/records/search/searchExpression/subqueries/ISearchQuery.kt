package com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Search query that represents Wallet Query Language (WQL) model
 */
sealed interface ISearchQuery {
    @Serializable
    sealed class SearchExpression<T>: HashMap<String, T>, ISearchQuery{

        constructor(): super(1)
        constructor(key: String, value: T){
            this[key] = value
        }

        override fun toString(): String {
            return Json.encodeToString(this as HashMap<String,T>)
        }

        @Serializable
        class MapExpression:  SearchExpression<String>{

            constructor(key: String, value: String){
                this[key] = value
            }
        }

        @Serializable
        class MapListExpression: SearchExpression<List<String>> {

            constructor(key: String, values: List<String>){
                this[key] = values
            }
        }

        @Serializable
        class EmptyExpression: SearchExpression<String> {

            constructor(){
                this[""] = ""
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String,String>)
            }

        }

        @Serializable
        class AndSubquery: SearchExpression<List<ISearchQuery>> {

            constructor(collection: List<ISearchQuery>){
                if (collection.size < 2) {
                    throw Exception("AND query must have 2 or more subqueries")
                }
                val key = "\$and"
                this[key] = collection
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, List<HashMap<String,String>>>)
            }
        }

        @Serializable
        class LikeSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this[name] = MapExpression("\$like", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String,String>>)
            }
        }

        @Serializable
        class GtSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this["~$name"] = MapExpression("\$gt", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String,String>>)
            }
        }

        @Serializable
        class NotEqSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this[name] = MapExpression("\$neq", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String,String>>)
            }
        }

        @Serializable
        class OrSubquery : SearchExpression<List<ISearchQuery>> {

            constructor(collection: List<ISearchQuery>) {
                if (collection.size < 2) {
                    throw Exception("OR query must have 2 or more subqueries")
                }
                val key = "\$or"
                this[key] = collection
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, List<HashMap<String,String>>>)
            }

        }

        @Serializable
        class GteSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this["~$name"] = MapExpression("\$gte", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String,String>>)
            }
        }

        @Serializable
        class LteSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this["~$name"] = MapExpression("\$lte", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String, String>>)
            }
        }

        @Serializable
        class InSubquery : SearchExpression<SearchExpression<List<String>>> {

            constructor(name: String, values: List<String>?) {
                if (values == null || !values.any()) {
                    throw Exception("IN query must have at least one value")
                }
                this[name] = MapListExpression("\$in", values)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String, HashMap<String, List<String>>>)
            }
        }

        @Serializable
        class EqSubquery : SearchExpression<String> {

            constructor(name: String, value: String) {
                this[name] = value
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String,String>)
            }

        }

        @Serializable
        class LtSubquery : SearchExpression<SearchExpression<String>> {

            constructor(name: String, value: String) {
                this["~$name"] = MapExpression("\$lt", value)
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String,HashMap<String,String>>)
            }
        }

        @Serializable
        class NotSubquery : SearchExpression<ISearchQuery> {

            constructor(query: ISearchQuery) {
                this["\$not"] = query
            }

            override fun toString(): String {
                return Json.encodeToString(this as HashMap<String,HashMap<String,String>>)
            }
        }
    }
}