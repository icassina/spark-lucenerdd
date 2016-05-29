/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zouzias.spark.lucenerdd.spatial

import java.io.{StringReader, StringWriter}

import com.spatial4j.core.context.SpatialContext
import com.spatial4j.core.context.jts.{JtsSpatialContext, JtsSpatialContextFactory}
import com.spatial4j.core.shape.Shape
import org.zouzias.spark.lucenerdd.config.ShapeLuceneRDDConfigurable

trait ContextLoader extends ShapeLuceneRDDConfigurable{

  protected val LocationDefaultField = getLocationFieldName

  protected lazy val shapeReader = ctx.getFormats().getReader(getShapeFormat)
  protected lazy val shapeWriter = ctx.getFormats().getWriter(getShapeFormat)

  protected def shapeToString(shape: Shape): String = {
    val writer = new StringWriter()
    shapeWriter.write(writer, shape)
    writer.toString
  }

  protected def stringToShape(shapeAsString: String): Shape = {
   shapeReader.read(new StringReader(shapeAsString))
  }

  /**
   * The Spatial4j {@link SpatialContext} is a sort of global-ish singleton
   * needed by Lucene spatial.  It's a facade to the rest of Spatial4j, acting
   * as a factory for {@link Shape}s and provides access to reading and writing
   * them from Strings.
   */
  protected lazy val ctx: JtsSpatialContext = ctxFactory.newSpatialContext() // SpatialContext.GEO

  protected lazy val ctxFactory = new JtsSpatialContextFactory()
}
