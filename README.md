# تطبيق حجز السيارات (Car Booking App) 🚗

تطبيق أندرويد متكامل ومصمم باحترافية لتسهيل عملية استئجار السيارات. يعتمد التطبيق على بنية **MVVM** القوية لضمان الأداء العالي وسهولة الصيانة، مع استخدام **Room Database** لتخزين البيانات محلياً.

---

## 📋 نظرة عامة على المشروع
المشروع عبارة عن منصة رقمية تربط المستخدمين بأسطول من السيارات المتنوعة. يغطي التطبيق الدورة الكاملة لعملية التأجير، بدءاً من استعراض السيارات، وصولاً إلى الحجز، الدفع، التقييم، ومتابعة الإشعارات.

### ✨ الميزات العامة
- **نظام مستخدمين متكامل**: تسجيل دخول، إنشاء حساب، وإدارة ملف شخصي.
- **بحث وفلترة متقدمة**: البحث حسب النوع، السعر، سنة الصنع، ونوع الوقود.
- **إدارة الحجوزات**: متابعة الحجوزات الحالية والسابقة.
- **نظام تقييم**: إمكانية تقييم السيارات بعد انتهاء فترة الحجز.
- **إشعارات ذكية**: تنبيهات فورية ومخزنة لمتابعة حالة الحجز.
- **دعم الوضع الليلي (Dark Mode)**: واجهة مريحة للعين تتكيف مع إعدادات النظام.

---

## 📱 شرح صفحات التطبيق (App Screens)

### 1. صفحة البداية والترحيب (Onboarding & Splash)
- **[SplashFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/SplashFragment.java)**: شاشة ترحيبية تظهر عند فتح التطبيق مع شعار المشروع.
- **[OnboardingFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/OnboardingFragment.java)**: تعرض ميزات التطبيق الأساسية للمستخدم الجديد من خلال واجهة تفاعلية (Slider).

### 2. نظام الهوية (Authentication)
- **[LoginFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/LoginFragment.java)**: تتيح للمستخدم الدخول إلى حسابه مع التحقق من البيانات عبر `UserRepository`.
- **[RegisterFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/RegisterFragment.java)**: لإنشاء حساب جديد وتخزين بيانات المستخدم في قاعدة البيانات المحلية.

### 3. الصفحة الرئيسية (Home Screen)
- **[HomeFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/HomeFragment.java)**: هي قلب التطبيق، تحتوي على:
    - شريط بحث سريع.
    - قائمة تصنيفات السيارات (Sedan, SUV, Luxury).
    - عرض قائمة السيارات المتاحة باستخدام `RecyclerView` و `CarAdapter`.
    - نظام فلترة متقدم يظهر في نافذة سفلية (Bottom Sheet).

### 4. تفاصيل السيارة والحجز (Car Details)
- **[CarDetailFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/CarDetailFragment.java)**: تعرض معلومات مفصلة عن السيارة (المواصفات، السعر اليومي، الوصف).
    - تتيح اختيار تاريخ البداية والنهاية للحجز.
    - حساب السعر الإجمالي تلقائياً.
    - إتمام عملية الحجز والدفع الوهمي.

### 5. إدارة الحجوزات (My Bookings)
- **[MyBookingsFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/MyBookingsFragment.java)**: لوحة تحكم تعرض الحجوزات مقسمة إلى:
    - **[CurrentBookingsFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/CurrentBookingsFragment.java)**: للحجوزات القائمة والمستقبلية.
    - **[PastBookingsFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/PastBookingsFragment.java)**: للسجل التاريخي للحجوزات مع خيار التقييم.

### 6. المفضلة والإشعارات (Features)
- **[FavoritesFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/FavoritesFragment.java)**: تعرض السيارات التي قام المستخدم بتمييزها للرجوع إليها لاحقاً.
- **[NotificationFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/NotificationFragment.java)**: تعرض قائمة بجميع الإشعارات المسجلة في قاعدة البيانات (مثل تأكيد الحجز، التذكيرات).

### 7. الملف الشخصي والإعدادات (Profile)
- **[ProfileFragment](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/fragment/ProfileFragment.java)**: تتيح للمستخدم:
    - تعديل بياناته الشخصية (الاسم، الهاتف).
    - تغيير كلمة المرور.
    - التحكم في إعدادات التطبيق (الوضع الليلي، اللغة).
    - تسجيل الخروج.

---

## 🛠️ التفاصيل التقنية (Technical Deep Dive)

### إدارة البيانات (Data Management)
- يتم استخدام **Room Database** مع 4 جداول أساسية:
    - `User`: لحفظ بيانات الحسابات.
    - `Car`: لحفظ بيانات السيارات ومواصفاتها.
    - `Booking`: لربط المستخدم بالسيارة وتواريخ الحجز.
    - `Notification`: لحفظ سجل التنبيهات.

### منطق الأعمال (Business Logic)
- **[BookingViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/BookingViewModel.java)**: هو المسؤول عن معالجة طلبات الحجز والتأكد من عدم وجود تعارض في المواعيد.
- **[AppViewModelFactory](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/AppViewModelFactory.java)**: يضمن توفير النسخ الصحيحة من الـ ViewModels مع كافة التبعيات اللازمة (Dependency Injection).

### التصميم (UI/UX)
- تم استخدام **Material Design 3** لضمان تجربة مستخدم عصرية وسلسة.
- استخدام `ConstraintLayout` لبناء واجهات مرنة تتناسب مع مختلف أحجام الشاشات.
- أيقونات مخصصة ورسوم توضيحية لتحسين التفاعل البصري.

---

## 📁 هيكلية المجلدات المحدثة
```text
app/src/main/java/com/example/car_booking_app/
├── data/               # طبقة البيانات (DAOs, Entities, Repositories)
├── ui/
│   ├── adapter/        # محولات البيانات للقوائم (RecyclerView)
│   ├── fragment/       # كافة واجهات التطبيق (Screens)
│   └── viewmodel/      # منطق الواجهات (MVVM Logic)
├── util/               # أدوات مساعدة (Session, Date Helpers)
└── workers/            # المهام الخلفية (WorkManager)
```

---

## 🚀 كيف يعمل نظام الحجز؟
1. يختار المستخدم السيارة من **HomeFragment**.
2. ينتقل إلى **CarDetailFragment** ويحدد التواريخ.
3. يقوم النظام بحساب التكلفة؛ عند الضغط على "Book Now"، يتم إنشاء سجل في جدول `Booking`.
4. يتم توليد إشعار فوري وحفظه في جدول `Notification`.
5. يظهر الحجز فوراً في صفحة **My Bookings**.
