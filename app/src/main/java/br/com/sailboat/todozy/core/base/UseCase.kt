package br.com.sailboat.todozy.core.base

import br.com.sailboat.todozy.core.failure.Failure
import br.com.sailboat.todozy.core.functional.Either

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend operator fun invoke(params: Params): Either<Failure, Type>
}