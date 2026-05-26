import SwiftUI
import Observation

// MARK: - Environment key

private struct AppColorsKey: EnvironmentKey {
    static let defaultValue: AppColors = AppTheme.goldDark.colors
}

extension EnvironmentValues {
    var appColors: AppColors {
        get { self[AppColorsKey.self] }
        set { self[AppColorsKey.self] = newValue }
    }
}

// MARK: - Theme manager

@Observable
class ThemeManager {
    var current: AppTheme

    init() {
        let saved = UserDefaults.standard.string(forKey: "selectedTheme") ?? "goldDark"
        current = AppTheme(rawValue: saved) ?? .goldDark
    }

    func set(_ theme: AppTheme) {
        current = theme
        UserDefaults.standard.set(theme.rawValue, forKey: "selectedTheme")
    }

    var colors: AppColors { current.colors }
}
