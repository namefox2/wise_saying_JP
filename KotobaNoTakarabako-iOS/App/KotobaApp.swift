import SwiftUI
import SwiftData

@main
struct KotobaApp: App {
    @State private var themeManager = ThemeManager()
    @State private var dataStore    = DataStoreManager.shared

    private let modelContainer: ModelContainer = {
        let schema = Schema([FavoriteItem.self])
        let config = ModelConfiguration(isStoredInMemoryOnly: false)
        return try! ModelContainer(for: schema, configurations: [config])
    }()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(themeManager)
                .environment(dataStore)
                .environment(\.appColors, themeManager.colors)
                .modelContainer(modelContainer)
                .onAppear { dataStore.updateStreak() }
        }
    }
}
