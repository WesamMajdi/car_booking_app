# 🎶 توثيق خدمات الخلفية (Background Services) في أندرويد

---

## 🛠️ المطلوب 1: إنشاء Service لتشغيل الموسيقى

### 1. كود الخدمة (`MusicService.java`)
تعتبر الـ `Service` مكوناً أساسياً لتنفيذ العمليات الطويلة في الخلفية دون الحاجة لواجهة مستخدم.

```java
package com.example.car_booking_app.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.car_booking_app.R;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // يتم إنشاء MediaPlayer وربطه بملف الموسيقى (يجب أن يكون في res/raw)
        // mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        Log.d("MusicService", "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // تشغيل الموسيقى عند بدء الخدمة
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.setLooping(true); // جعل الموسيقى تعمل بشكل مستمر
            mediaPlayer.start();
        }
        
        // START_STICKY: تعني إذا قتل النظام الخدمة، فسيقوم بإعادة إنشائها مرة أخرى
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // إيقاف وتحرير الموارد عند إغلاق الخدمة
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.d("MusicService", "Service Destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
```

### 2. التحكم بالخدمة من الـ `Activity`
لبدء أو إيقاف الخدمة من أي واجهة (مثل `MainActivity`):

```java
// لبدء الخدمة (تشغيل الموسيقى)
Intent startIntent = new Intent(this, MusicService.class);
startService(startIntent);

// لإيقاف الخدمة (إغلاق الموسيقى)
Intent stopIntent = new Intent(this, MusicService.class);
stopService(stopIntent);
```

---

## ❓ المطلوب 2: ماذا يحدث عند خروج المستخدم من التطبيق؟

**الإجابة:** نعم، ستستمر الموسيقى في العمل حتى بعد خروج المستخدم من التطبيق.

**لماذا؟**
1.  **طبيعة الـ Service**: تم تصميم خدمات أندرويد (Started Services) لتعمل بشكل مستقل عن دورة حياة الواجهة (`Activity`). بمجرد بدء الخدمة باستخدام `startService()`، فإنها تظل تعمل في الخلفية حتى لو تم تدمير الـ `Activity`.
2.  **استقلال العملية**: تظل الخدمة نشطة طالما أن نظام أندرويد لم يقم بقتلها لتوفير الذاكرة. باستخدام `START_STICKY` في كود `onStartCommand` نضمن عودة الموسيقى للعمل حتى لو تم إغلاقها قسراً من النظام.

---

## 📁 المطلوب 3: هل قمت بتنفيذ شيء مشابه في المشروع؟

**نعم، تم تنفيذ منطق مشابه في مشروعنا باستخدام `WorkManager`.**

### التفاصيل التقنية:
في مشروعنا، قمنا بإنشاء [BookingNotificationWorker](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/worker/BookingNotificationWorker.java).

**كيف تم تنفيذه؟**
1.  **العمل في الخلفية**: تماماً مثل الـ `Service` التي تشغل الموسيقى، يقوم الـ `Worker` بالعمل في الخلفية للتحقق من مواعيد الحجوزات القادمة.
2.  **التكرار والاستمرارية**: يعمل الـ `Worker` بشكل دوري حتى لو كان التطبيق مغلقاً تماماً أو حتى بعد إعادة تشغيل الهاتف، مما يضمن وصول التنبيهات للمستخدم في الوقت المناسب.
3.  **الفرق الجوهري**: استخدمنا `WorkManager` بدلاً من `Service` العادية لأنه أكثر كفاءة في استهلاك البطارية وموصى به من قبل Google للمهام التي لا تتطلب تشغيلاً مستمراً (مثل تشغيل الموسيقى) بل تتطلب تنفيذ مهام محددة في أوقات معينة.

---

### ملاحظة هامة للمطورين:
لجعل الخدمة تعمل بشكل احترافي في النسخ الحديثة من أندرويد (Android 8.0+)، يجب تحويلها إلى **Foreground Service** وإظهار إشعار مستمر (Notification) للمستخدم لتجنب قتلها من قبل النظام.
