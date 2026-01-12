# Laporan Lengkap Proyek Aplikasi SkillSwap

## Bab 1: Pendahuluan

### 1.1 Deskripsi Umum Aplikasi
SkillSwap adalah sebuah prototipe aplikasi mobile Android fungsional yang dibangun menggunakan Java. Aplikasi ini berfungsi sebagai platform yang memungkinkan pengguna untuk menemukan individu lain guna melakukan pertukaran keahlian (skill swapping). Ide dasarnya adalah menciptakan sebuah komunitas di mana pengguna dapat menawarkan keahlian yang mereka miliki dan mencari keahlian yang ingin mereka pelajari, lalu berinteraksi melalui sistem perpesanan terintegrasi.

### 1.2 Latar Belakang & Tujuan Proyek
Proyek ini dikembangkan sebagai tugas akhir mata kuliah Pemrograman Mobile. Tujuannya adalah untuk mengaplikasikan secara praktis konsep-konsep fundamental dan modern dalam pengembangan aplikasi Android, yang mencakup:
- **Manajemen State & UI**: Mengelola tampilan antarmuka yang dinamis dan responsif.
- **Navigasi**: Mengimplementasikan sistem navigasi yang intuitif (Bottom Navigation dan perpindahan antar Activity).
- **Penyimpanan Data Lokal**: Menggunakan database lokal untuk menyimpan data pengguna dan aplikasi secara persisten.
- **Integrasi Hardware API**: Memanfaatkan fungsionalitas perangkat keras seperti GPS untuk fitur berbasis lokasi.
- **Arsitektur Kode**: Merancang struktur proyek yang bersih, terorganisir, dan mudah dikelola.

---

## Bab 2: Arsitektur dan Desain Sistem

### 2.1 Arsitektur Aplikasi
Secara konseptual, arsitektur aplikasi ini mengadopsi prinsip **Model-View-Controller (MVC)** yang disederhanakan dan disesuaikan untuk Android, di mana:
- **Model**: Direpresentasikan oleh **Room Persistence Library**. Ini adalah lapisan data yang bertanggung jawab atas semua operasi database.
  - `UserEntity.java`, `ChatEntity.java`: Mendefinisikan skema atau tabel database.
  - `UserDao.java`, `ChatDao.java`: Bertindak sebagai *Data Access Object* yang menyediakan *interface* untuk berinteraksi dengan tabel.
  - `AppDatabase.java`: Merupakan *entry point* utama untuk mengakses seluruh database.
- **View**: Terdiri dari file layout XML (`activity_login.xml`, `fragment_home.xml`, dll.) yang mendefinisikan seluruh komponen antarmuka pengguna.
- **Controller**: Diimplementasikan di dalam kelas `Activity` dan `Fragment` (`LoginActivity.java`, `HomeFragment.java`, dll.). Kelas-kelas ini bertugas merespons interaksi pengguna, memproses logika bisnis, dan memanipulasi data melalui **Model** untuk kemudian diperbarui di **View**.

### 2.2 Desain Database (Room)
Aplikasi ini menggunakan dua tabel utama:
1.  **`users_table` (`UserEntity`)**: Menyimpan data kredensial dan profil pengguna.
    - `id`: Primary Key (auto-increment).
    - `fullName`: Nama lengkap pengguna.
    - `email`: Email untuk login.
    - `password`: Kata sandi.
    - `credits`: Integer untuk menyimpan jumlah kredit yang dimiliki pengguna.
2.  **`chat_history` (`ChatEntity`)**: Menyimpan riwayat "inbox" atau daftar percakapan.
    - `id`: Primary Key.
    - `partnerName`: Nama partner chat.
    - `lastMessage`: Pesan terakhir untuk ditampilkan di inbox.
    - `profileImageResId`: ID resource gambar profil untuk ditampilkan di inbox.

---

## Bab 3: Analisis Detail Fungsionalitas dan Logika

Bagian ini menguraikan alur kerja teknis dari setiap fitur utama.

### 3.1 Fitur: Autentikasi Pengguna
**Tujuan**: Memastikan hanya pengguna terdaftar yang dapat mengakses fitur utama aplikasi.

- **Logika Registrasi (`RegisterActivity.java`)**:
  1.  Pengguna mengisi form (nama, email, password) dan menekan tombol `btn_register`.
  2.  Fungsi `onClickListener` mengambil teks dari setiap `EditText`.
  3.  Dilakukan validasi: memastikan kolom tidak kosong dan password cocok.
  4.  Aplikasi mengakses database: `AppDatabase.getInstance(this)`.
  5.  Mengecek apakah email sudah ada dengan memanggil `db.userDao().getUserByEmail(email)`.
  6.  Jika belum ada, sebuah objek `new UserEntity(fullName, email, password, 100)` dibuat. Kredit awal di-set ke **100**.
  7.  Objek pengguna baru disimpan ke database melalui `db.userDao().registerUser(newUser)`.
  8.  Sesi dibuat dengan menyimpan nama pengguna ke `SharedPreferences` (`sharedPref.edit().putString("USER_NAME", ...)`).
  9.  Pengguna diarahkan ke `LoadingActivity`.

- **Logika Login (`LoginActivity.java`)**:
  1.  Pengguna memasukkan email dan password, lalu menekan `btn_login`.
  2.  Fungsi `onClickListener` memanggil `db.userDao().login(email, password)`.
  3.  DAO menjalankan query `SELECT * FROM users_table WHERE ...`.
  4.  Jika query mengembalikan objek `UserEntity` (tidak null), login berhasil. Nama pengguna disimpan ke `SharedPreferences` sebagai penanda sesi.
  5.  Jika null, `Toast` "Email atau password salah" ditampilkan.

- **Manajemen Sesi (`SplashActivity.java`)**:
  1.  Saat aplikasi pertama kali dibuka, `SplashActivity` memeriksa `SharedPreferences`.
  2.  `sharedPref.getString("USER_NAME", null)`.
  3.  Jika hasilnya **tidak null**, berarti ada sesi aktif, dan pengguna langsung diarahkan ke `MainActivity`.
  4.  Jika **null**, pengguna diarahkan ke `LoginActivity`.

### 3.2 Fitur: Pencarian & Filter Partner di Beranda
**Tujuan**: Memungkinkan pengguna menemukan partner yang relevan dengan cepat.

- **Pemuatan Daftar Awal (`HomeFragment.java`)**:
  1.  Dalam `onCreateView`, fungsi `setupTopSwappers(view)` dipanggil.
  2.  Di dalamnya, sebuah `ArrayList<Swapper>` diinisialisasi dengan data dummy (Arfianda, Elon Musk, dll.), termasuk nama, skill, dan ID foto profil dari `R.drawable`.
  3.  `TopSwappersAdapter` diinisialisasi dengan daftar ini dan di-set ke `RecyclerView`.

- **Logika Pencarian Real-time (`HomeFragment.java`)**:
  1.  `SearchView` diberi `OnQueryTextListener`.
  2.  Setiap kali pengguna mengetik (`onQueryTextChange`), fungsi `filterSwappers(newText)` dipanggil.
  3.  `filterSwappers` melakukan iterasi pada **daftar asli** (`allSwappersList`), bukan daftar yang sudah difilter.
  4.  Untuk setiap `Swapper`, dilakukan pengecekan `s.getName().toLowerCase().contains(...)`.
  5.  Jika cocok, `Swapper` ditambahkan ke `filteredList` baru.
  6.  `swapperAdapter.updateList(filteredList)` dipanggil. Di dalam adapter, daftar lama dibersihkan dan daftar baru ditambahkan, lalu `notifyDataSetChanged()` dipanggil untuk me-render ulang UI.

- **Logika Filter Lintas Fragmen (`CategoryFragment` -> `HomeFragment`)**:
  1.  Di `CategoryFragment`, setiap item kategori (misal: Koding) memiliki `onClickListener`.
  2.  Saat diklik, nama kategori ("Koding") disimpan ke `SharedPreferences` dengan key `"LAST_FILTER"`.
  3.  Navigasi dipaksa pindah ke tab Beranda: `bottomNav.setSelectedItemId(R.id.nav_home)`.
  4.  `HomeFragment` menjadi aktif, sehingga siklus `onResume()` terpanggil.
  5.  Di dalam `onResume()`, fungsi `updateUI()` dijalankan.
  6.  `updateUI()` memeriksa apakah ada nilai untuk key `"LAST_FILTER"` di `SharedPreferences`.
  7.  Jika ada, ia memanggil `filterSwappers(...)` dengan nilai tersebut, lalu segera menghapus key `"LAST_FILTER"` dari `SharedPreferences` agar filter tidak berjalan lagi saat kembali ke Beranda lain waktu.

### 3.3 Fitur: Sistem Kredit dan Transaksi Pertukaran
**Tujuan**: Mensimulasikan sistem ekonomi sederhana untuk memoderasi interaksi.

- **Logika Pengurangan Kredit (`DetailSwapperActivity.java`)**:
  1.  Saat tombol `btn_chat_request` diklik, fungsi `handleSwapRequest()` dieksekusi.
  2.  Nama pengguna saat ini diambil dari `SharedPreferences`.
  3.  Menggunakan nama tersebut, data lengkap pengguna diambil dari database: `db.userDao().getUserByName(currentUserName)`.
  4.  Dilakukan pengecekan: `if (currentUser.credits >= 10)`.
  5.  Jika `true`, kredit dikurangi: `currentUser.credits -= 10`.
  6.  Objek `currentUser` yang sudah diperbarui dikirim kembali ke database: `db.userDao().updateUser(currentUser)`.
  7.  Sebagai tanda transaksi berhasil, sebuah entri baru dibuat di tabel chat melalui `db.chatDao().insert(...)`.

- **Pembaruan Tampilan Kredit (`HomeFragment.java`)**:
  1.  Setiap kali `HomeFragment` ditampilkan (`onResume`), `updateUI()` mengambil data pengguna terbaru dari Room DB.
  2.  Nilai `user.credits` kemudian di-set ke `TextView` `tv_main_credit`.
  3.  Ini memastikan bahwa setelah transaksi, jumlah kredit di Beranda selalu yang terbaru.

---

## Bab 4: Kesimpulan dan Pengembangan Lanjutan

### 4.1 Kesimpulan
Proyek aplikasi SkillSwap ini berhasil mengimplementasikan seluruh tujuan yang telah ditetapkan. Fungsionalitas inti seperti autentikasi berbasis database lokal, pencarian dinamis, interaksi antar pengguna, dan penggunaan API lokasi telah tercapai. Arsitektur yang diterapkan memisahkan data dari tampilan, menghasilkan kode yang lebih terstruktur dan mudah dipahami. Aplikasi ini berfungsi sebagai bukti nyata dari pemahaman dan penerapan konsep-konsep kunci dalam Pemrograman Mobile Android menggunakan Java.

### 4.2 Saran Pengembangan Lanjutan
Untuk menjadikan aplikasi ini sebagai produk skala penuh, beberapa pengembangan lanjutan dapat dilakukan:
- **Migrasi ke Backend Online**: Mengganti Room Database dengan layanan backend seperti **Firebase Firestore** untuk data real-time dan **Firebase Authentication** untuk login via Google/Email.
- **Notifikasi Push**: Menggunakan **Firebase Cloud Messaging (FCM)** untuk mengirim notifikasi instan saat ada pesan baru atau permintaan pertukaran.
- **Sistem Rating & Ulasan**: Menambahkan fitur bagi pengguna untuk saling memberi rating setelah sesi pertukaran selesai.
- **Unggah Gambar**: Memungkinkan pengguna mengunggah foto profil dari galeri HP menggunakan **Intent** dan menyimpannya di **Firebase Storage**.

---

## Bab 5: Panduan Instalasi dan Penggunaan

1.  **Clone Repository**:
    ```bash
    git clone https://github.com/ajyss/APP_SKILSWAP_PEMROGRAMAN_MOBILE.git
    ```
2.  **Buka di Android Studio**: Buka folder proyek melalui Android Studio (versi Bumblebee atau lebih baru).
3.  **Sync Gradle**: Tunggu proses sinkronisasi dan pengunduhan dependensi selesai.
4.  **Run Aplikasi**: Jalankan aplikasi pada Emulator (API 30+) atau perangkat Android fisik.
