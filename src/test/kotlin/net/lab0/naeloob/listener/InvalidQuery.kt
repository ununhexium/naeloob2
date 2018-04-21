package net.lab0.naeloob.listener

class InvalidQuery(msg: String, e: Throwable?) : Exception(msg, e)
{
    constructor(msg: String) : this(msg, null)
}
