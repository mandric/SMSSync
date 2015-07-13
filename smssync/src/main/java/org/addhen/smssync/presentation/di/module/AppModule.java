/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
 * Website: http://www.ushahidi.com
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Ushahidi developers at team@ushahidi.com.
 */

package org.addhen.smssync.presentation.di.module;

import com.addhen.android.raiburari.presentation.di.module.ApplicationModule;

import org.addhen.smssync.data.cache.FileManager;
import org.addhen.smssync.data.repository.FilterDataRepository;
import org.addhen.smssync.data.repository.LogDataRepository;
import org.addhen.smssync.domain.repository.FilterRepository;
import org.addhen.smssync.domain.repository.LogRepository;
import org.addhen.smssync.presentation.App;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Reusable Dagger modules for the entire app
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Module(includes = ApplicationModule.class)
public class AppModule {

    App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    @Singleton
    FilterRepository provideFilterRepository(
            FilterDataRepository filterDataRepository) {
        return filterDataRepository;
    }

    @Provides
    @Singleton
    LogRepository provideLogRepository(LogDataRepository logDataRepository) {
        return logDataRepository;
    }

    @Provides
    @Singleton
    FileManager provideFileManager(Context context) {
        return new FileManager(context);
    }
}