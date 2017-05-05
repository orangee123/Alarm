package org.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class MyAlarmManager extends Activity {
	private AlarmManager alarm = null; // 闹钟服务
	private Button set = null;
	private Button delete = null;
	private TextView msg = null;
	private TimePicker time = null;
	private int hourOfDay = 0 ;
	private int minute = 0;
	private Calendar calendar = Calendar.getInstance() ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main);
		this.set = (Button) super.findViewById(R.id.set);
		this.delete = (Button) super.findViewById(R.id.delete);
		this.msg = (TextView) super.findViewById(R.id.msg);
		this.time = (TimePicker) super.findViewById(R.id.time);
		
		this.alarm = (AlarmManager) super.getSystemService(Context.ALARM_SERVICE) ;
		this.set.setOnClickListener(new SetOnClickListener()) ;
		this.delete.setOnClickListener(new DeleteOnClickListener()) ;
		this.time.setIs24HourView(true) ;
		this.time.setOnTimeChangedListener(new OnTimeChangedListenerImpl()) ;
		
	}
	
	private class OnTimeChangedListenerImpl implements OnTimeChangedListener{

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			MyAlarmManager.this.calendar.setTimeInMillis(System.currentTimeMillis()) ;
			MyAlarmManager.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay) ;
			MyAlarmManager.this.calendar.set(Calendar.MINUTE, minute) ;
			MyAlarmManager.this.calendar.set(Calendar.SECOND, 0) ;
			MyAlarmManager.this.calendar.set(Calendar.MILLISECOND, 0) ;
			MyAlarmManager.this.hourOfDay = hourOfDay ;
			MyAlarmManager.this.minute = minute ;
		}
		
	}
	
	private class SetOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyAlarmManager.this,
					MyAlarmReceiver.class);
			intent.setAction("org.lxh.action.setalarm") ;
			PendingIntent sender = PendingIntent.getBroadcast(
					MyAlarmManager.this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			MyAlarmManager.this.alarm.set(AlarmManager.RTC_WAKEUP,
					MyAlarmManager.this.calendar.getTimeInMillis(), sender);
			MyAlarmManager.this.msg.setText("闹钟响起的时间是："
					+ MyAlarmManager.this.hourOfDay + "时"
					+ MyAlarmManager.this.minute + "分。");
			Toast.makeText(MyAlarmManager.this, "闹钟设置成功！",
					Toast.LENGTH_LONG).show();
		}
		
	}
	private class DeleteOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (MyAlarmManager.this.alarm != null) {
				Intent intent = new Intent(MyAlarmManager.this,
						MyAlarmReceiver.class);
				intent.setAction("org.lxh.action.setalarm") ;
				PendingIntent sender = PendingIntent.getBroadcast(
						MyAlarmManager.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				MyAlarmManager.this.alarm.cancel(sender) ;	// 取消
				MyAlarmManager.this.msg.setText("当前没有设置闹钟。") ;
				Toast.makeText(MyAlarmManager.this, "闹钟删除成功！",
						Toast.LENGTH_LONG).show();
			}
		}
		
	}
}