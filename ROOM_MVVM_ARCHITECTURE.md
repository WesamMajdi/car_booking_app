# 🏗️ شرح معمارية Room و MVVM في أندرويد

---

## 🛠️ المطلوب 1: دور كل طبقة عند استخدام Room

### 1. طبقة الـ DAO (Data Access Object)
هي الواجهة البرمجية (Interface) التي تحتوي على الميثودز المستخدمة للوصول إلى قاعدة البيانات.
- **دورها**: تحويل استعلامات SQL إلى ميثودز بلغة Java/Kotlin.
- **أهميتها**: تمنع كتابة استعلامات SQL داخل الـ Activity أو الـ Fragment، وتوفر فحصاً للطلبات أثناء عملية البناء (Compile-time validation).

### 2. طبقة الـ Repository
هي كلاس وسيط يعمل كـ "Single Source of Truth".
- **دورها**: إدارة مصادر البيانات المختلفة (مثل قاعدة البيانات المحلية أو API خارجي).
- **أهميتها**: تقوم بتجريد (Abstract) مصادر البيانات عن الـ ViewModel، مما يسهل استبدال مصدر البيانات أو اختباره.

### 3. طبقة الـ ViewModel
هي الطبقة المسؤولة عن منطق الواجهة (UI Logic).
- **دورها**: جلب البيانات من الـ Repository وتجهيزها للعرض عبر `LiveData` أو `Flow`.
- **أهميتها**: تحافظ على البيانات عند تدوير الشاشة (Configuration Changes) وتفصل منطق العمل عن دورة حياة الـ Activity.

---

## 🔗 كيف يتم الربط؟

### 1. ربط Room مع Repository:
يتم ذلك عن طريق تمرير الـ **DAO** كبارامتر في مُنشئ (Constructor) الـ Repository.
```java
public class UserRepository {
    private UserDao userDao;
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

### 2. ربط Repository مع ViewModel:
يتم ذلك عن طريق تمرير الـ **Repository** في مُنشئ الـ ViewModel (عادةً عبر `ViewModelFactory`).
```java
public class AuthViewModel extends ViewModel {
    private UserRepository repository;
    public AuthViewModel(UserRepository repository) {
        this.repository = repository;
    }
}
```

---

## 📁 المطلوب 2: هل استخدمت Room داخل مشروعك؟

**نعم، المشروع مبني بالكامل على نمط MVVM واستخدام Room Database.**

### كيف قمت بالربط في مشروعنا (بالتفصيل):

1.  **قاعدة البيانات (Database)**:
    قمنا بإنشاء [CarDatabase](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/database/CarDatabase.java) التي تعرّف الجداول والـ DAOs.

2.  **طبقة الـ Repository**:
    أنشأنا مستودعات مثل [BookingRepository](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/data/repository/BookingRepository.java). هذا المستودع يستقبل `BookingDao` و `CarDao` للقيام بعمليات الحجز والتقييم.

3.  **طبقة الـ ViewModel**:
    أنشأنا [BookingViewModel](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/BookingViewModel.java) الذي يطلب البيانات من `BookingRepository`.

4.  **الربط النهائي (Dependency Injection)**:
    استخدمنا [AppViewModelFactory](file:///c:/Users/HP/AndroidStudioProjects/car_booking_app/app/src/main/java/com/example/car_booking_app/ui/viewmodel/AppViewModelFactory.java) كحلقة وصل أساسية. يقوم الـ Factory بـ:
    - جلب نسخة من قاعدة البيانات.
    - إنشاء الـ DAOs.
    - تمرير الـ DAOs إلى الـ Repository.
    - تمرير الـ Repository إلى الـ ViewModel.

**مثال من الكود الفعلي للمشروع:**
```java
// داخل AppViewModelFactory.java
CarDatabase db = CarDatabase.getDatabase(application);
UserRepository userRepository = new UserRepository(db.userDao());
// يتم تمرير المستودع للـ ViewModel
return (T) new AuthViewModel(userRepository);
```

هذا النمط يضمن أن التطبيق منظم، سهل الصيانة، وقابل للتوسع بشكل احترافي.
