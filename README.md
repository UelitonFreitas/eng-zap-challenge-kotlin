# eng-zap-challenge-kotlin
Zap code challenge

## Tests

You can check CI status in [this](https://github.com/UelitonFreitas/eng-zap-challenge-kotlin/actions) link.

### Unit tests

The tests wore focused on main business rules in `VivaRealScreenPresenter`, `ZapScreenPresenter` and `PropertyDetailScreen`.


## To Do
A lot of things can be done. You can find some of then here:
- A UX person with good heath to create a descent layout :)
- Support offline mode.
  Create a Cache repository that receives the remote and a persistent(Room or other). When recovering from API save in persistent repository and return persisted data. 
- Add a Dependency Injection framework like Koin if needed. The architecture try to follow Dependency Inversion on objects, so will be ease to adapt.
- Optimize app layout and resources
- Create async property load in Property Detail Screen. 
- It is possible to get the property from the repository and use deep links for better code encapsulations.
- Besides Retrofit makes the request out of Main Thread, it is possible to use Coroutines.
- Add more Espresso tests using persisted data to create integration tests of UI and contract tests. We can use different flavors for this objective.
- Automatic upload of APK to store using Kotlin DSL scripts or Fastlane.
- Create appropriated errors and stop using Exceptions.
- Limit properties list to avoid memory crash since API don't implement pagination.
- It is possible to reuse more code in presenter but i'm for my opinions is safest to use composition instead inheritance. So will be nice to extract the common behavior to functions or components and inject them.
