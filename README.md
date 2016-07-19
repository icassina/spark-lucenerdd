# spark-lucenerdd

[![Master](https://travis-ci.org/zouzias/spark-lucenerdd.svg?branch=master)](https://travis-ci.org/zouzias/spark-lucenerdd)
[![Coverage Status](https://coveralls.io/repos/github/zouzias/spark-lucenerdd/badge.svg?branch=master)](https://coveralls.io/github/zouzias/spark-lucenerdd?branch=master)
[![Maven](https://img.shields.io/maven-central/v/org.zouzias/spark-lucenerdd_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.zouzias/spark-lucenerdd_2.11/)
[![Javadocs](http://javadoc.io/badge/org.zouzias/spark-lucenerdd_2.11:0.0.14.svg?color=yellowgreen)](http://javadoc.io/doc/org.zouzias/spark-lucenerdd_2.11/)


Spark RDD with Apache [Lucene](https://lucene.apache.org)'s query capabilities.

The main abstraction is a special type of `RDD` called `LuceneRDD`, which instantiates a Lucene index on each Spark executor (a.k.a. worker).

`LuceneRDD` collects and aggregates search results from Spark executors to the Spark driver. Currently, the following queries are supported:

|Operation| Syntax| Description |
|-------|---------------------|----------|
|Term query     | `LuceneRDD.termQuery(field, query, topK)`| Exact term search |
|Fuzzy query | `LuceneRDD.fuzzyQuery(field, query, maxEdits, topK)`| Fuzzy term search |
|Phrase query | `LuceneRDD.phraseQuery(field, query, topK)` | Phrase search |
|Prefix query | `LuceneRDD.prefixSearch(field, prefix, topK)` | Prefix search |
|Query parser | `LuceneRDD.query(queryString, topK)` | Query parser search|
|Faceted search| `LuceneRDD.facetQuery(queryString, field, topK)` | Faceted Search |
|[Record Linkage](https://github.com/zouzias/spark-lucenerdd/wiki/Record-Linkage-with-LuceneRDD)| `LuceneRDD.link(otherEntity: RDD[T], linkageFunction: T => searchQuery, topK)`| Record linkage via Lucene queries|

Using the query parser, you can perform prefix queries, fuzzy queries, prefix queries, etc. 
For more information on using Lucene's query parser, see [Query Parser](https://lucene.apache.org/core/5_5_0/queryparser/org/apache/lucene/queryparser/classic/QueryParser.html). 

For example, using the query parser you can perform prefix queries on the field named textField and prefix query 
`spar` as `LuceneRDD.query("textField:spar*", 10)`.

### Examples

Here are a few examples using `LuceneRDD` for full text search, spatial search and record linkage. All examples exploit Lucene's flexible query language. For spatial search, `lucene-spatial` and `jts` is required.

* [Text Search](https://github.com/zouzias/spark-lucenerdd/wiki/Text-search-with-LuceneRDD)
* [Spatial Search](https://github.com/zouzias/spark-lucenerdd/wiki/Spatial-search-using-ShapeLuceneRDD)
* [Record Linkage](https://github.com/zouzias/spark-lucenerdd/wiki/Record-Linkage-with-LuceneRDD)

For more, check the [wiki](https://github.com/zouzias/spark-lucenerdd/wiki)

## Linking

You can link against this library (for Spark 1.4+) in your program at the following coordinates:

Using SBT:

```
libraryDependencies += "org.zouzias" %% "spark-lucenerdd" % "0.0.XX"
```

Using Maven:

```xml
<dependency>
    <groupId>org.zouzias</groupId>
    <artifactId>spark-lucenerdd_2.11</artifactId>
    <version>0.0.XX</version>
</dependency>
```

This library can also be added to Spark jobs launched through `spark-shell` or `spark-submit` by using the `--packages` command line option.
For example, to include it when starting the spark shell:

```
$ bin/spark-shell --packages org.zouzias:spark-lucenerdd_2.11:0.0.XX
```

Unlike using `--jars`, using `--packages` ensures that this library and its dependencies will be added to the classpath.
The `--packages` argument can also be used with `bin/spark-submit`.

This library is cross-published for Scala 2.11, so 2.11 users should replace 2.10 with 2.11 in the commands listed above.

### Project Status and Limitations

Currently the Lucene index is only stored in memory.

Implicit conversions for the primitive types (Int, Float, Double, Long, String) are supported. Moreover, implicit conversions for all product types (i.e., tuples and case classes) of the above primitives are supported. Implicits for tuples default the field names to "_1", "_2", "_3, ... following Scala's naming conventions for tuples.

### Custom Case Classes

If you want to use your own custom class with `LuceneRDD` you can do it provided that your class member types are one of the above primitive types (Int, Float, Double, Long, String).

For more details, see `LuceneRDDCustomcaseClassImplicits` under the tests directory.


## Requirements

This library requires Spark 1.4+ and Java 7+.

### Development

Install Java, [SBT](http://www.scala-sbt.org) and clone the project

```bash
git clone https://github.com/zouzias/spark-lucenerdd.git
cd spark-lucenerdd
sbt compile assembly
```

The above will create an assembly jar containing spark-lucenerdd functionality under `target/scala-*/spark-lucenerdd-assembly-*.jar`

To make the spark-lucenerdd available, you have to assembly the project and add the JAR on you Spark shell or submit scripts.
