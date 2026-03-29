# 🍽️ توثيق خدمات تطبيقات المطاعم والإشعارات في أندرويد

---

## 🛠️ المطلوب 1: أي نوع من الـ Service سنستخدم لمتابعة طلبات المطعم؟

**الإجابة:** النوع الأمثل لهذه الحالة هو **Foreground Service**.

### لماذا Foreground Service؟
1.  **الأولوية العالية**: بما أن التطبيق يحتاج لتتبع الطلبات حتى عند إغلاقه، فإن الـ `Foreground Service` تخبر نظام أندرويد أن هذه العملية ذات أهمية قصوى للمستخدم، مما يمنع النظام من قتلها لتوفير الذاكرة.
2.  **الشفافية**: تتطلب هذه الخدمة إظهار إشعار مستمر (Persistent Notification) للمستخدم، مما يجعله على علم بأن التطبيق لا يزال يعمل في الخلفية لمتابعة طلبه.
3.  **الاستمرارية**: تضمن استمرار العمل حتى لو خرج المستخدم من التطبيق تماماً.

---

### كود الخدمة لتحديث الإشعارات كل 10 ثوانٍ:

```java
public class OrderTrackingService extends Service {
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // تحويل الخدمة إلى Foreground Service (يتطلب إشعار)
        startForeground(1, createNotification("جاري تتبع طلبك..."));

        runnable = new Runnable() {
            @Override
            public void run() {
                checkNewOrders(); // ميثود وهمية لفحص الطلبات
                handler.postDelayed(this, 10000); // تكرار كل 10 ثوانٍ
            }
        };
        handler.post(runnable);
        
        return START_STICKY;
    }

    private void checkNewOrders() {
        // منطق جلب البيانات من السيرفر
        Log.d("OrderService", "Checking for new orders...");
    }
}
```

### كيف نتعامل مع استهلاك البطارية؟
1.  **استخدام الـ Interval المناسب**: بدلاً من 10 ثوانٍ ثابتة، يمكن استخدام **Exponential Backoff** (زيادة الوقت بين الفحص والآخر إذا لم تكن هناك طلبات جديدة).
2.  **استخدام FCM (Firebase Cloud Messaging)**: بدلاً من جعل التطبيق يفحص السيرفر باستمرار (Polling)، نستخدم الإشعارات السحابية التي توقظ التطبيق فقط عند وجود طلب جديد، وهو الحل الأفضل للبطارية.
3.  **القيود**: الالتزام بقيود نظام أندرويد الحديثة (Doze Mode) التي تفرض ترشيد استهلاك الطاقة.

---

## 📁 المطلوب 2: هل استخدمت Service في مشروعك؟

**نعم، قمنا باستخدام أنواع مختلفة من الخدمات والعمليات الخلفية في مشروعنا.**

### 1. الـ Service التقليدية:
قمنا بإنشاء [MusicService](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/service/MusicService.java) لتشغيل الموسيقى في الخلفية، وهي تعمل بنمط **Started Service** لتستمر حتى بعد خروج المستخدم.

### 2. البديل المتطور (WorkManager):
في الجزء الخاص بإشعارات الحجوزات، فضلنا استخدام [BookingNotificationWorker](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/worker/BookingNotificationWorker.java).

**لماذا استخدمنا WorkManager بدلاً من Foreground Service في الحجوزات؟**
- **الكفاءة**: لأن تتبع الحجز لا يتطلب عملاً مستمراً كل ثانية، بل يتطلب فحصاً في أوقات محددة.
- **الاستدامة**: الـ `WorkManager` يضمن تنفيذ المهمة حتى لو أُغلق الهاتف أو أُعيد تشغيله، وهو الحل الموصى به من Google للمهام المجدولة.

**الخلاصة**: استخدمنا الـ `Service` للموسيقى (عمل مستمر) واستخدمنا الـ `Worker` للتنبيهات (عمل مجدول)، وهذا يظهر فهماً عميقاً لكيفية اختيار الأداة المناسبة لكل وظيفة في أندرويد.
