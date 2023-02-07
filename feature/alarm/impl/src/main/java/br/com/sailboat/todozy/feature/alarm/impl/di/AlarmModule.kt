package br.com.sailboat.todozy.feature.alarm.impl.di

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.SaveAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerServiceImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.CancelAlarmScheduleUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.CancelAlarmScheduleUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.DeleteAlarmUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.GetAlarmUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.GetNextAlarmUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.SaveAlarmUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarmUpdatesUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarmUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAllAlarmsUseCaseImpl
import br.com.sailboat.todozy.feature.alarm.impl.presentation.mapper.AlarmToAlarmUiModelMapperImpl
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<AlarmToAlarmUiModelMapper> { AlarmToAlarmUiModelMapperImpl(get()) }
}

private val domain = module {
    factory<ScheduleAlarmUseCase> { ScheduleAlarmUseCaseImpl(get()) }
    factory<ScheduleAllAlarmsUseCase> { ScheduleAllAlarmsUseCaseImpl(get(), get(), get()) }
    factory<ScheduleAlarmUpdatesUseCase> { ScheduleAlarmUpdatesUseCaseImpl(get()) }
    factory<CancelAlarmScheduleUseCase> { CancelAlarmScheduleUseCaseImpl(get()) }
    factory<SaveAlarmUseCase> { SaveAlarmUseCaseImpl(get(), get(), get()) }
    factory<GetAlarmUseCase> { GetAlarmUseCaseImpl(get()) }
    factory<DeleteAlarmUseCase> { DeleteAlarmUseCaseImpl(get(), get()) }
    factory<GetNextAlarmUseCase> { GetNextAlarmUseCaseImpl() }
}

private val data = module {
    factory<AlarmRepository> { AlarmRepositoryImpl(get(), get(), get()) }
    factory<AlarmLocalDataSource> { AlarmLocalDataSourceSQLite(get()) }
    factory<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
    factory { AlarmDataToAlarmMapper() }
    factory { AlarmToAlarmDataMapper() }
}

val alarmModule: List<Module> = listOf(presentation, domain, data)
