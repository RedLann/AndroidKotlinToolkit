package com.github.redlann.cleanarchitecture

interface Repository

abstract class BaseUseCase<out Type, in Params>(val repository: Repository) where Type : Any {
    open fun run(params: Params): Type {
        TODO("not implemented")
    }

    open suspend fun runSuspend(params: Params): Type {
        TODO("not implemented")
    }
}