/*****************************************************************************
 ** Copyright (c) 2010 - 2012 Ushahidi Inc
 ** All rights reserved
 ** Contact: team@ushahidi.com
 ** Website: http://www.ushahidi.com
 **
 ** GNU Lesser General Public License Usage
 ** This file may be used under the terms of the GNU Lesser
 ** General Public License version 3 as published by the Free Software
 ** Foundation and appearing in the file LICENSE.LGPL included in the
 ** packaging of this file. Please review the following information to
 ** ensure the GNU Lesser General Public License version 3 requirements
 ** will be met: http://www.gnu.org/licenses/lgpl.html.
 **
 **
 ** If you have questions regarding the use of this file, please contact
 ** Ushahidi developers at team@ushahidi.com.
 **
 *****************************************************************************/

package org.addhen.smssync;

import java.util.ArrayList;

import org.addhen.smssync.database.Database;
import org.addhen.smssync.net.MainHttpClient;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

/**
 * This class is for maintaining global application state.
 * 
 * @author eyedol
 */
public class MainApplication extends Application {

	public static final String TAG = "SmsSyncApplication";

	public static Database mDb;

	public static MainHttpClient mApi;

	public static Application app = null;

	public static int currentConnectionIndex = -1;
	public static ArrayList<MessengerConnection> availableConnections = new ArrayList<MessengerConnection>();

	public static MessengerConnection[] messengerConnectionList = new MessengerConnection[5];
	public static ServiceConnection[] serviceConnectionList = new ServiceConnection[5];

	public ServiceConnection getServiceConnection(final MessengerConnection m) {
		ServiceConnection mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				m.messenger = new Messenger(service);
				m.isBound = true;
				availableConnections.add(m);
			}

			public void onServiceDisconnected(ComponentName className) {
				m.messenger = null;
				m.isBound = false;
				availableConnections.remove(m);
			}
		};
		return mConnection;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Open database connection when the application starts.
		app = this;
		mDb = new Database(this);
		mDb.open();

		if (!availableConnections.contains(null)) {
			availableConnections.add(null);
		}
		Intent senderIntent_0 = new Intent("com.smssync.portal.action.SEND_SMS");
		messengerConnectionList[0] = new MessengerConnection();
		serviceConnectionList[0] = getServiceConnection(messengerConnectionList[0]);
		bindService(senderIntent_0, serviceConnectionList[0],
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onTerminate() {
		// Close the database when the application terminates.
		mDb.close();
		super.onTerminate();
		// UnBind from all the SMS senders
		for (int x = 0; x < messengerConnectionList.length; x++) {
			if (messengerConnectionList[x].isBound) {
				unbindService(serviceConnectionList[x]);
				messengerConnectionList[x].isBound = false;
			}
		}
	}
}