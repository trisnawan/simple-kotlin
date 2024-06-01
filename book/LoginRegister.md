## Session Set
```kotlin
val sharedPreferences: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
val editor: SharedPreferences.Editor = sharedPreferences.edit()
editor.putString("key", "value")
```

## Session Get
```kotlin
val sharedPreferences: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
sharedPreferences.getString("key", null)
```
