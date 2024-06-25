package com.alterok.mausamlive.core.data.remote

class ApiKeyNotFoundException : Throwable("Api-Key not found!")
class UnauthorizedKeyException : Throwable("Unauthorized! Key Invalid!")
