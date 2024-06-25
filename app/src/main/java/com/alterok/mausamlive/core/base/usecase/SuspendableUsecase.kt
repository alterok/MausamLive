package com.alterok.mausamlive.core.base.usecase

interface SuspendableUsecase<in Data, out Result> {
    suspend operator fun invoke(data: Data): Result
}