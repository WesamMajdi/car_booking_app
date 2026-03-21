# تطبيق حجز السيارات (Car Booking App) 🚗

تطبيق أندرويد متكامل يتيح للمستخدمين استكشاف، حجز، وإدارة تأجير السيارات بسهولة. يتميز التطبيق بتصميم عصري (Material Design 3) ونظام إدارة بيانات قوي باستخدام Room Database.

## 📋 فكرة المشروع
يهدف التطبيق إلى تسهيل عملية استئجار السيارات من خلال توفير منصة تتيح للمستخدم:
- تصفح مجموعة واسعة من السيارات بمختلف الأنواع (سيدان، SUV، رياضية، فاخرة).
- فلترة السيارات بناءً على السعر، نوع الوقود، سنة الصنع، وعدد الركاب.
- نظام حجز متكامل يتضمن اختيار التواريخ، الدفع، وتأكيد الحجز.
- نظام إشعارات ذكي لمتابعة حالة الحجوزات.
- نظام تقييم (Rating) لمشاركة تجربة المستخدم.

---

## 🚀 التقنيات المستخدمة (Tech Stack)
- **اللغة**: Java (Android SDK).
- **واجهة المستخدم (UI)**: XML مع Material Components.
- **إدارة البيانات (Database)**: [Room Persistence Library](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/database/CarDatabase.java).
- **إدارة الحالة (State Management)**: ViewModel & LiveData.
- **التنقل (Navigation)**: Jetpack Navigation Component.
- **إدارة الجلسات**: SharedPreferences.
- **المهام الخلفية**: WorkManager (للإشعارات الدورية).

---

## 🛠️ هيكلية الكود (Architecture)
يتبع المشروع نمط **MVVM (Model-ViewModel-ViewModel)** لضمان فصل المنطق عن الواجهة:

### 1. طبقة البيانات (Data Layer)
- **الكيانات (Entities)**: تمثل جداول قاعدة البيانات مثل `User` و `Car` و `Booking` و `Notification`.
- **الوصول للبيانات (DAOs)**: واجهات برمجية للتعامل مع SQLite (إدخال، حذف، استعلام).
- **المستودعات (Repositories)**: طبقة وسيطة تدير تدفق البيانات بين قاعدة البيانات والـ ViewModel.

### 2. طبقة المنطق (ViewModel Layer)
- **[AuthViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/AuthViewModel.java)**: يدير عمليات تسجيل الدخول والتسجيل والملف الشخصي.
- **[CarViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/CarViewModel.java)**: يدير عرض السيارات وعمليات الفلترة والبحث.
- **[BookingViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/BookingViewModel.java)**: يدير عمليات الحجز والتقييم.
- **[NotificationViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/NotificationViewModel.java)**: يدير عرض قائمة الإشعارات.

### 3. طبقة الواجهة (UI Layer)
- **Fragments**: مثل `HomeFragment` و `CarDetailFragment` و `MyBookingsFragment`.
- **Adapters**: لإدارة عرض القوائم في RecyclerView (مثل `CarAdapter` و `BookingAdapter`).

---

## ✨ الميزات الرئيسية المنفذة في الكود

### 1. نظام الحجز الذكي
عندما يقوم المستخدم بحجز سيارة، يقوم الكود بـ:
- التحقق من توفر السيارة.
- حساب السعر الإجمالي بناءً على عدد الأيام.
- حفظ الحجز في قاعدة البيانات.
- إنشاء إشعار محلي وتخزينه ليظهر في قائمة الإشعارات.

### 2. نظام التقييم بالنجوم (Star Rating)
تم تنفيذ نظام تقييم يسمح للمستخدم بتقييم تجربته بعد انتهاء الحجز:
- يظهر زر "Rate Experience" في قائمة الحجوزات.
- يفتح نافذة منبثقة (`RatingDialog`) لاختيار عدد النجوم.
- يتم تحديث متوسط تقييم السيارة تلقائياً في قاعدة البيانات.

### 3. الفلترة المتقدمة (Advanced Filtering)
تسمح واجهة البحث للمستخدم بتخصيص النتائج:
- فلترة حسب سنة الصنع (Year).
- فلترة حسب السعر (Price Range).
- فلترة حسب نوع المحرك (Engine Type).

### 4. نظام الإشعارات (Persistent Notifications)
خلافاً للإشعارات العادية التي تختفي، يمتلك التطبيق:
- جدول `Notification` في قاعدة البيانات لحفظ تاريخ الإشعارات.
- واجهة مخصصة لعرض كافة التنبيهات السابقة.

---

## 📁 هيكل المجلدات
```text
app/src/main/java/com/example/car_booking_app/
├── data/
│   ├── dao/          # واجهات الوصول للبيانات (Room DAOs)
│   ├── database/     # تهيئة قاعدة البيانات (Room DB)
│   ├── entity/       # نماذج البيانات (Entities)
│   └── repository/   # المستودعات (Repositories)
├── ui/
│   ├── adapter/      # محولات القوائم (RecyclerView Adapters)
│   ├── fragment/     # واجهات التطبيق (Fragments)
│   └── viewmodel/    # منطق الأعمال (ViewModels)
└── util/             # أدوات مساعدة (SessionManager, Helpers)
```

---

## 📝 ملاحظات للمطورين
- المشروع يستخدم **AppViewModelFactory** بنمط Singleton لضمان كفاءة حقن التبعيات (Dependency Injection).
- تم تفعيل ميزة **fallbackToDestructiveMigration** في قاعدة البيانات لتسهيل عملية التطوير وتحديث الجداول.
