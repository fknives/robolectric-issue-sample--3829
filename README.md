# robolectric-issue-sample--3829

Sample for issue https://github.com/robolectric/robolectric/issues/3829, comment: https://github.com/robolectric/robolectric/issues/3829#issuecomment-1013515751.

Reference files: 
- [CrashBecauseOfTimeOut](https://github.com/fknives/robolectric-issue-sample--3829/blob/develop/app/src/test/java/org/fnives/android/timeout/issue/report/CrashBecauseOfTimeOut.kt)
- [NoCrashWithoutTimeOut](https://github.com/fknives/robolectric-issue-sample--3829/blob/develop/app/src/test/java/org/fnives/android/timeout/issue/report/NoCrashWithoutTimeOut.kt)

Versions
- Robolectric version: 4.7.3
- Espresso version: 3.4.0
- androidx.test.ext:junit: 1.1.3
- junit version: 4.13.2

## Workaround
### Thanks to [hoisie](https://github.com/hoisie) in this [comment](https://github.com/robolectric/robolectric/issues/3829#issuecomment-1013549820)
I've created a TestRule which mimicks  the TimeOut test rule and seems to be working fine for shared tests for now.
- See the class here: [SharedTimeOut](https://github.com/fknives/robolectric-issue-sample--3829/blob/develop/app/src/sharedTest/java/org/fnives/android/timout/issue/report/fix/SharedTimeOut.kt)
- How it was verified: [SharedTimeOutTest](https://github.com/fknives/robolectric-issue-sample--3829/blob/develop/app/src/sharedTest/java/org/fnives/android/timout/issue/report/fix/SharedTimeOutTest.kt)
