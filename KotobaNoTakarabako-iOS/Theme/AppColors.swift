import SwiftUI

struct AppColors {
    var background: Color
    var surface: Color
    var surfaceAlt: Color
    var accent: Color
    var accentSecondary: Color
    var text: Color
    var textSecondary: Color
    var textTertiary: Color
    var border: Color
    var cardBg: Color
    var heartActive: Color
    var tabBarBg: Color
    var shadow: Color
}

enum AppTheme: String, CaseIterable, Identifiable {
    case goldDark    = "goldDark"
    case sakuraDark  = "sakuraDark"
    case oceanDark   = "oceanDark"
    case washiLight  = "washiLight"

    var id: String { rawValue }

    var displayName: String {
        switch self {
        case .goldDark:   return "골드 다크"
        case .sakuraDark: return "벚꽃 다크"
        case .oceanDark:  return "오션 다크"
        case .washiLight: return "화지 라이트"
        }
    }

    var colors: AppColors {
        switch self {
        case .goldDark:
            return AppColors(
                background:     Color(hex: "#1A1209"),
                surface:        Color(hex: "#2D2210"),
                surfaceAlt:     Color(hex: "#3D3018"),
                accent:         Color(hex: "#D4A843"),
                accentSecondary:Color(hex: "#F0C060"),
                text:           Color(hex: "#F5E6C8"),
                textSecondary:  Color(hex: "#C8B090"),
                textTertiary:   Color(hex: "#907858"),
                border:         Color(hex: "#5A4020"),
                cardBg:         Color(hex: "#251A0C"),
                heartActive:    Color(hex: "#FF6B6B"),
                tabBarBg:       Color(hex: "#1A1209"),
                shadow:         Color(hex: "#000000").opacity(0.5)
            )
        case .sakuraDark:
            return AppColors(
                background:     Color(hex: "#1A0F14"),
                surface:        Color(hex: "#2D1A22"),
                surfaceAlt:     Color(hex: "#3D2230"),
                accent:         Color(hex: "#E8739C"),
                accentSecondary:Color(hex: "#F5A0C0"),
                text:           Color(hex: "#F5E0EA"),
                textSecondary:  Color(hex: "#C8A0B8"),
                textTertiary:   Color(hex: "#907080"),
                border:         Color(hex: "#5A2040"),
                cardBg:         Color(hex: "#251018"),
                heartActive:    Color(hex: "#FF4080"),
                tabBarBg:       Color(hex: "#1A0F14"),
                shadow:         Color(hex: "#000000").opacity(0.5)
            )
        case .oceanDark:
            return AppColors(
                background:     Color(hex: "#0A1520"),
                surface:        Color(hex: "#102030"),
                surfaceAlt:     Color(hex: "#183040"),
                accent:         Color(hex: "#4DA8DA"),
                accentSecondary:Color(hex: "#70C8F0"),
                text:           Color(hex: "#D0E8F5"),
                textSecondary:  Color(hex: "#90B8D0"),
                textTertiary:   Color(hex: "#506878"),
                border:         Color(hex: "#204060"),
                cardBg:         Color(hex: "#0E1E2E"),
                heartActive:    Color(hex: "#FF6B6B"),
                tabBarBg:       Color(hex: "#0A1520"),
                shadow:         Color(hex: "#000000").opacity(0.5)
            )
        case .washiLight:
            return AppColors(
                background:     Color(hex: "#F5F0E8"),
                surface:        Color(hex: "#EDE8DC"),
                surfaceAlt:     Color(hex: "#E5DED0"),
                accent:         Color(hex: "#8B4513"),
                accentSecondary:Color(hex: "#A0522D"),
                text:           Color(hex: "#2C1A0E"),
                textSecondary:  Color(hex: "#5A3820"),
                textTertiary:   Color(hex: "#907050"),
                border:         Color(hex: "#C8B090"),
                cardBg:         Color(hex: "#F8F4EC"),
                heartActive:    Color(hex: "#CC2222"),
                tabBarBg:       Color(hex: "#EDE8DC"),
                shadow:         Color(hex: "#000000").opacity(0.15)
            )
        }
    }
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 6: (a, r, g, b) = (255, (int >> 16) & 0xFF, (int >> 8) & 0xFF, int & 0xFF)
        case 8: (a, r, g, b) = ((int >> 24) & 0xFF, (int >> 16) & 0xFF, (int >> 8) & 0xFF, int & 0xFF)
        default: (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(.sRGB, red: Double(r)/255, green: Double(g)/255, blue: Double(b)/255, opacity: Double(a)/255)
    }
}
