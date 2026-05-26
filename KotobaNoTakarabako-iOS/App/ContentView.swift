import SwiftUI

struct ContentView: View {
    @Environment(\.appColors) var colors

    var body: some View {
        TabView {
            HomeView()
                .tabItem { Label("홈", systemImage: "house.fill") }

            QuoteCardView(category: "전체")
                .tabItem { Label("명언", systemImage: "quote.bubble.fill") }

            JlptView()
                .tabItem { Label("JLPT", systemImage: "textbook.fill") }

            FavoritesView()
                .tabItem { Label("즐겨찾기", systemImage: "heart.fill") }

            SettingsView()
                .tabItem { Label("설정", systemImage: "gearshape.fill") }
        }
        .tint(colors.accent)
    }
}
