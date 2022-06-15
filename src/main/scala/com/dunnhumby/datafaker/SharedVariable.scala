package com.dunnhumby.datafaker

import scala.reflect.ClassTag


class SharedVariable[T: ClassTag](constructor: => T) extends AnyRef with Serializable {

  @transient private lazy val instance = new ThreadLocal[T]()

  def get = {
    if (instance.get() == null) {
      instance.set(constructor)
    }
    instance.get()
  }

}

object SharedVariable {

  def apply[T: ClassTag](constructor: => T): SharedVariable[T] = new SharedVariable[T](constructor)

}