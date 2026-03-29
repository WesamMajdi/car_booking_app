# 🔔 دليل إنشاء الإشعارات (Notifications) في أندرويد

---

## 🛠️ المطلوب 1: الخطوات الأساسية لإنشاء إشعار

لإنشاء إشعار احترافي في أندرويد، نتبع الخطوات التالية:

### 1. إنشاء قناة الإشعار (Notification Channel)
هذه الخطوة إلزامية لإصدارات أندرويد 8.0 (API 26) فما فوق.
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
        "CHANNEL_ID",           // المعرف الفريد للقناة
        "Channel Name",         // الاسم الذي يظهر للمستخدم في الإعدادات
        NotificationManager.IMPORTANCE_HIGH // مستوى الأهمية
    );
    NotificationManager manager = getSystemService(NotificationManager.class);
    manager.createNotificationChannel(channel);
}
```

### 2. استخدام `NotificationCompat.Builder`
نقوم بضبط خصائص الإشعار الأساسية:
- **Icon**: الأيقونة الصغيرة التي تظهر في شريط التنبيهات.
- **Title**: العنوان الرئيسي للإشعار.
- **Text**: محتوى الرسالة التفصيلي.

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_notification) // 3. الأيقونة (Icon)
        .setContentTitle("تأكيد الحجز")            // 1. العنوان (Title)
        .setContentText("تم حجز سيارتك بنجاح!")    // 2. النص (Text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true); // يختفي الإشعار عند الضغط عليه
```

### 3. إظهار الإشعار عبر `NotificationManager`
```java
NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
notificationManager.notify(NOTIFICATION_ID, builder.build());
```

---

## ❓ المطلوب 2: ما هو مفهوم الـ Notification Channel ولماذا هو مهم؟

**المفهوم:**
قنوات الإشعارات (Notification Channels) هي ميزة قدمتها Google في إصدار **Android 8.0 (Oreo)**. تهدف إلى تقسيم إشعارات التطبيق إلى مجموعات منطقية.

**الأهمية في الإصدارات الحديثة:**
1.  **تحكم المستخدم**: تسمح للمستخدم بتعطيل نوع معين من الإشعارات (مثل الإعلانات) مع إبقاء نوع آخر (مثل تنبيهات الحجز) فعالاً، بدلاً من تعطيل إشعارات التطبيق بالكامل.
2.  **تخصيص التنبيه**: يمكن للمستخدم تخصيص الصوت، الاهتزاز، ومستوى الأهمية لكل قناة على حدة من إعدادات النظام.
3.  **إلزامية النظام**: لن تظهر الإشعارات إطلاقاً في الإصدارات الحديثة إذا لم تكن مرتبطة بقناة صالحة.

---

## 📁 المطلوب 3: هل استخدمت Notification Channels في مشروعك؟

**نعم، تم استخدامها وتطبيقها بشكل احترافي.**

### كيف قمت بربطها في المشروع؟
قمنا بإنشاء كلاس مخصص يسمى [NotificationHelper](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/util/NotificationHelper.java) لإدارة هذه العملية.

**تفاصيل الربط في الكود الفعلي:**
1.  **تعريف القناة**: قمنا بتعريف قناة خاصة بالحجوزات:
    - المعرف: `booking_notifications`
    - الاسم: `Booking Notifications`
2.  **التحقق من الإصدار**: داخل ميثود `showNotification` نقوم دائماً بفحص إصدار الأندرويد:
    ```java
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(CHANNEL_DESC);
        notificationManager.createNotificationChannel(channel);
    }
    ```
3.  **الاستخدام في الحجز**: يتم استدعاء هذه الميثود فور نجاح عملية الحجز في الـ `Fragment` لتنبيه المستخدم فوراً.

هذا الربط يضمن أن التطبيق متوافق مع أحدث معايير أندرويد ويوفر للمستخدم أفضل تجربة تحكم في التنبيهات.
