import Foundation
import Observation

@Observable
class DataStoreManager {
    static let shared = DataStoreManager()

    var selectedTheme: String { didSet { save(selectedTheme, key: Keys.theme) } }
    var fontSize: Double      { didSet { save(fontSize, key: Keys.fontSize) } }
    var autoPlay: Bool        { didSet { save(autoPlay, key: Keys.autoPlay) } }
    var autoBlur: Bool        { didSet { save(autoBlur, key: Keys.autoBlur) } }
    var notificationsEnabled: Bool { didSet { save(notificationsEnabled, key: Keys.notifications) } }
    var notificationHour: Int      { didSet { save(notificationHour, key: Keys.notifHour) } }
    var notificationMinute: Int    { didSet { save(notificationMinute, key: Keys.notifMinute) } }
    var streakCount: Int      { didSet { save(streakCount, key: Keys.streak) } }
    var lastVisitDate: Date?  { didSet { if let d = lastVisitDate { save(d, key: Keys.lastVisit) } } }

    private enum Keys {
        static let theme         = "selectedTheme"
        static let fontSize      = "fontSize"
        static let autoPlay      = "autoPlay"
        static let autoBlur      = "autoBlur"
        static let notifications = "notificationsEnabled"
        static let notifHour     = "notifHour"
        static let notifMinute   = "notifMinute"
        static let streak        = "streakCount"
        static let lastVisit     = "lastVisitDate"
    }

    private init() {
        let d = UserDefaults.standard
        selectedTheme      = d.string(forKey: Keys.theme)                    ?? "goldDark"
        fontSize           = (d.object(forKey: Keys.fontSize) as? Double)    ?? 1.0
        autoPlay           = d.bool(forKey: Keys.autoPlay)
        autoBlur           = (d.object(forKey: Keys.autoBlur) as? Bool)      ?? true
        notificationsEnabled = d.bool(forKey: Keys.notifications)
        notificationHour   = (d.object(forKey: Keys.notifHour) as? Int)      ?? 8
        notificationMinute = (d.object(forKey: Keys.notifMinute) as? Int)    ?? 0
        streakCount        = d.integer(forKey: Keys.streak)
        lastVisitDate      = d.object(forKey: Keys.lastVisit) as? Date
    }

    private func save(_ value: any Any, key: String) {
        UserDefaults.standard.set(value, forKey: key)
    }

    func updateStreak() {
        let today = Calendar.current.startOfDay(for: Date())
        guard let last = lastVisitDate else {
            streakCount = 1
            lastVisitDate = today
            return
        }
        let diff = Calendar.current.dateComponents([.day], from: Calendar.current.startOfDay(for: last), to: today).day ?? 0
        if diff == 1 {
            streakCount += 1
            lastVisitDate = today
        } else if diff > 1 {
            streakCount = 1
            lastVisitDate = today
        }
    }
}
