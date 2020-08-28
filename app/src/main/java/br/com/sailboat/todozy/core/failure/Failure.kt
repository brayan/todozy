package br.com.sailboat.todozy.core.failure

sealed class Failure {
    object EntityNotFoundFailure : Failure()

    abstract class FeatureFailure : Failure()
}