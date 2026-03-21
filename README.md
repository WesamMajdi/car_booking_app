# 🚗 تطبيق حجز السيارات الذكي (Car Rental Hub)

![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Language](https://img.shields.io/badge/Language-Java-orange?logo=java)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)
![Database](https://img.shields.io/badge/Database-Room-brightgreen)

تطبيق أندرويد متطور يوفر تجربة سلسة واحترافية لاستئجار السيارات، مصمم بأحدث معايير تجربة المستخدم (UX) وأقوى التقنيات البرمجية لضمان الثبات والأداء.

---

## � حول المشروع
هذا المشروع ليس مجرد تطبيق بسيط، بل هو منصة متكاملة لإدارة تأجير السيارات محلياً. يركز التطبيق على توفير واجهة سهلة للمستخدم لاستكشاف السيارات المتاحة، إتمام عمليات الحجز، ومتابعة سجلاته التاريخية، مع نظام تنبيهات ذكي يبقيه على اطلاع دائم.

---

## 🌟 الميزات الجوهرية (Key Features)

### 👤 إدارة المستخدمين (Identity Management)
- **نظام تسجيل دخول آمن**: يعتمد على `SessionManager` و `SharedPreferences`.
- **الملف الشخصي التفاعلي**: إمكانية تعديل البيانات، تغيير الصورة الشخصية، والتحكم في إعدادات التطبيق.

### 🔍 استكشاف السيارات (Car Discovery)
- **قائمة السيارات الديناميكية**: عرض السيارات مع تفاصيلها الكاملة (السعر، النوع، المحرك، عدد الركاب).
- **الفلترة المتقدمة (Filter Engine)**: فلترة حسب سنة الصنع، نطاق السعر، ونوع الوقود.
- **البحث اللحظي**: العثور على أي سيارة بمجرد كتابة اسمها.

### 📅 نظام الحجز والجدولة (Booking System)
- **حجز مرن**: اختيار تواريخ الاستلام والتسليم مع حساب تلقائي للسعر.
- **إدارة الحجوزات**: تقسيم الحجوزات إلى (حالية - سابقة) لتسهيل المتابعة.
- **نظام التقييم**: إمكانية تقييم كل حجز بعد انتهائه لتعزيز الثقة.

### 🔔 نظام التنبيهات (Notification Engine)
- **إشعارات لحظية**: تنبيهات عند نجاح الحجز أو تحديث حالته.
- **سجل الإشعارات**: واجهة خاصة لاستعراض كافة التنبيهات السابقة والمخزنة في قاعدة البيانات.

---

## �️ البناء التقني (Technical Stack)

- **اللغة الأساسية**: Java (Android SDK).
- **بنية التطبيق**: **MVVM (Model-ViewModel-Intent)** لضمان فصل منطق الأعمال عن الواجهة.
- **قاعدة البيانات**: **Room Persistence Library** (تخزين محلي سريع وآمن).
- **التنقل**: **Jetpack Navigation Component** (إدارة التنقل بين الشاشات بسلاسة).
- **الواجهات الرسومية**: **Material Design 3** مع استخدام `ConstraintLayout` و `MotionLayout`.
- **المهام الخلفية**: **WorkManager** لإدارة المهام التي تعمل في الخلفية مثل الإشعارات الدورية.

---

## 🏗️ التصميم البرمجي (Architecture & Design)

### طبقة البيانات (Data Layer)
تتكون من 5 جداول مترابطة في قاعدة البيانات:
1.  **[User](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/entity/User.java)**: يخزن بيانات الحساب الشخصي.
2.  **[Car](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/entity/Car.java)**: يحتوي على كافة تفاصيل السيارة ومواصفاتها.
3.  **[Booking](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/entity/Booking.java)**: يربط المستخدم بالسيارة مع التواريخ والحالة.
4.  **[Notification](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/entity/Notification.java)**: سجل التنبيهات الخاص بكل مستخدم.
5.  **[BookingWithCar](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/entity/BookingWithCar.java)**: نموذج علاقة (Relation) لجلب بيانات الحجز مع تفاصيل السيارة المرتبطة به.

---

## 📱 رحلة المستخدم داخل التطبيق (App Workflow)

### 1️⃣ شاشة البداية (Splash & Onboarding)
يبدأ المستخدم برؤية شعار التطبيق ثم شاشات تعريفية تشرح له كيفية الاستخدام والفوائد التي سيحصل عليها.

### 2️⃣ الصفحة الرئيسية (Home Screen)
هنا يجد المستخدم قائمة السيارات الأكثر طلباً، مع إمكانية استخدام شريط البحث أو الفلاتر لتخصيص النتائج بناءً على احتياجاته.

### 3️⃣ تفاصيل السيارة (Car Details)
عند اختيار سيارة، تظهر صفحة تحتوي على صور السيارة، مواصفاتها الفنية، وسعرها. يمكن للمستخدم هنا اختيار تواريخ الحجز والضغط على "Book Now".

### 4️⃣ إتمام الحجز (Booking Process)
يتم التحقق من البيانات، ثم يظهر إشعار نجاح الحجز. يتم حفظ هذا الحجز في قسم "My Bookings" وإرسال تنبيه إلى سجل الإشعارات.

### 5️⃣ إدارة الملف الشخصي (Profile Management)
يمكن للمستخدم تخصيص تجربته، تغيير لغة التطبيق، أو التبديل بين الوضع الفاتح والداكن (Dark Mode).

---

## 📁 هيكلية المجلدات (Project Directory Structure)

```text
car_booking_app/
├── app/
│   ├── src/main/java/com/example/car_booking_app/
│   │   ├── data/           # الطبقة المسؤولة عن البيانات (DAOs, Entities, Repositories)
│   │   │   ├── dao/        # واجهات الاستعلام عن قاعدة البيانات
│   │   │   ├── database/   # تهيئة Room Database
│   │   │   └── entity/     # نماذج البيانات (Models)
│   │   ├── ui/             # الطبقة المسؤولة عن واجهة المستخدم والمنطق المرتبط بها
│   │   │   ├── adapter/    # محولات القوائم (RecyclerView Adapters)
│   │   │   ├── fragment/   # كافة شاشات التطبيق (Fragments)
│   │   │   └── viewmodel/  # منطق الأعمال وإدارة الحالة (ViewModels)
│   │   └── util/           # أدوات مساعدة (Session Manager, Date Utils)
│   └── src/main/res/       # كافة الموارد (Layouts, Drawables, Values)
└── README.md               # هذا الملف التوثيقي
```

---

## 🚀 كيفية تشغيل المشروع (Setup)

1.  قم بعمل `Clone` للمستودع:
    ```bash
    git clone https://github.com/WesamMajdi/car_booking_app.git
    ```
2.  افتح المشروع باستخدام **Android Studio (Giraffe أو أحدث)**.
3.  انتظر حتى ينتهي الـ `Gradle Sync`.
4.  قم بتشغيل التطبيق على `Emulator` أو جهاز حقيقي بنظام أندرويد 8.0 فما فوق.

---

## 📝 ملاحظات إضافية
- التطبيق يدعم اللغتين **العربية والإنجليزية**.
- تم استخدام **AppViewModelFactory** بنمط Singleton لضمان حقن التبعيات بشكل صحيح وتوفير استهلاك الذاكرة.
- قاعدة البيانات معدة لتقبل التحديثات المستقبلية دون فقدان بيانات المستخدم (Migration Ready).

---

**تم تطوير هذا المشروع بشغف لتقديم أفضل تجربة حجز سيارات ممكنة.** 💡
