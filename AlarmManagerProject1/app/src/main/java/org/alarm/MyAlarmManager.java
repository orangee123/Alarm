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
	private AlarmManager alarm = null; // 闹钟管理
	private Button set = null;//按钮组件
	private Button delete = null;//按钮组件
	private TextView msg = null;//文本显示组件
	private TimePicker time = null;//时间选择器
	private int hourOfDay = 0 ;//保存设置的时
	private int minute = 0;//保存设置的分
	private Calendar calendar = Calendar.getInstance() ;//取得Calender对象

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//调用布局管理器
		super.setContentView(R.layout.main);//取得时间选择器
		this.set = (Button) super.findViewById(R.id.set);//取得按钮组件
		this.delete = (Button) super.findViewById(R.id.delete);//取得按钮组件
		this.msg = (TextView) super.findViewById(R.id.msg);//取得文本显示组件
		this.time = (TimePicker) super.findViewById(R.id.time); //取得时间选择器
		
		this.alarm = (AlarmManager) super.getSystemService(Context.ALARM_SERVICE) ;//取得闹钟服务
		this.set.setOnClickListener(new SetOnClickListener()) ;
		this.delete.setOnClickListener(new DeleteOnClickListener()) ;
		this.time.setIs24HourView(true) ;
		this.time.setOnTimeChangedListener(new OnTimeChangedListenerImpl()) ;//设置时间改变监听
		
	}
	
	private class OnTimeChangedListenerImpl implements OnTimeChangedListener{

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			MyAlarmManager.this.calendar.setTimeInMillis(System.currentTimeMillis()) ;//设置当前时间
			MyAlarmManager.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay) ;
			MyAlarmManager.this.calendar.set(Calendar.MINUTE, minute) ;
			MyAlarmManager.this.calendar.set(Calendar.SECOND, 0) ;
			MyAlarmManager.this.calendar.set(Calendar.MILLISECOND, 0) ;
			MyAlarmManager.this.hourOfDay = hourOfDay ;//保存
			MyAlarmManager.this.minute = minute ;
		}
		
	}
	
	private class SetOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyAlarmManager.this,
					MyAlarmReceiver.class);//指定跳转的Intent
			intent.setAction("org.campass.action.setalarm") ;//定义广播的Action
			PendingIntent sender = PendingIntent.getBroadcast(
					MyAlarmManager.this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);//指定PendingIntent
			MyAlarmManager.this.alarm.set(AlarmManager.RTC_WAKEUP,
					MyAlarmManager.this.calendar.getTimeInMillis(), sender);//设置闹钟
			MyAlarmManager.this.msg.setText("闹钟响起的时间是："
					+ MyAlarmManager.this.hourOfDay + "时"
					+ MyAlarmManager.this.minute + "分。");
			Toast.makeText(MyAlarmManager.this, "闹钟设置成功！",
					Toast.LENGTH_LONG).show();//显示提示信息
		}
		
	}
	private class DeleteOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (MyAlarmManager.this.alarm != null) {
				Intent intent = new Intent(MyAlarmManager.this,
						MyAlarmReceiver.class);//设置Intent
				intent.setAction("org.campass.action.setalarm") ;
				PendingIntent sender = PendingIntent.getBroadcast(
						MyAlarmManager.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);//指定PendingIntent
				MyAlarmManager.this.alarm.cancel(sender) ;	// 取消闹钟
				MyAlarmManager.this.msg.setText("当前没有设置闹钟。") ;
				Toast.makeText(MyAlarmManager.this, "闹钟删除成功！",
						Toast.LENGTH_LONG).show();
			}
		}
		
	}
}