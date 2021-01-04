package br.com.mouzinho.starwarspopcode.presentation.util.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler
    fun main(): Scheduler
    fun computation(): Scheduler
}