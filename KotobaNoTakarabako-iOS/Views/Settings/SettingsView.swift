import SwiftUI

struct SettingsView: View {
    @Environment(\.appColors) var colors
    @Environment(ThemeManager.self) var themeManager
    @Environment(DataStoreManager.self) var dataStore
    @State private var vm = SettingsViewModel()

    private let fontOptions: [(String, Double)] = [
        ("작게", 0.85), ("보통", 1.0), ("크게", 1.2), ("매우 크게", 1.4)
    ]

    var body: some View {
        NavigationStack {
            ZStack {
                colors.background.ignoresSafeArea()

                List {
                    themeSection
                    fontSection
                    learningSection
                    notificationSection
                    dataSection
                    streakSection
                }
                .scrollContentBackground(.hidden)
            }
            .navigationTitle("설정")
        }
    }

    // MARK: - Sections

    private var themeSection: some View {
        Section("테마") {
            ForEach(AppTheme.allCases) { theme in
                Button { themeManager.set(theme) } label: {
                    HStack {
                        themePreview(theme)
                        Text(theme.displayName).foregroundColor(colors.text)
                        Spacer()
                        if themeManager.current == theme {
                            Image(systemName: "checkmark").foregroundColor(colors.accent)
                        }
                    }
                }
            }
        }
        .listRowBackground(colors.surface)
    }

    private func themePreview(_ theme: AppTheme) -> some View {
        HStack(spacing: 2) {
            Rectangle().fill(theme.colors.background).frame(width: 8, height: 20)
            Rectangle().fill(theme.colors.accent).frame(width: 8, height: 20)
        }
        .clipShape(RoundedRectangle(cornerRadius: 3))
    }

    private var fontSection: some View {
        Section("폰트 크기") {
            ForEach(fontOptions, id: \.0) { name, scale in
                Button { dataStore.fontSize = scale } label: {
                    HStack {
                        Text(name)
                            .font(.system(size: 14 * scale))
                            .foregroundColor(colors.text)
                        Spacer()
                        if dataStore.fontSize == scale {
                            Image(systemName: "checkmark").foregroundColor(colors.accent)
                        }
                    }
                }
            }
        }
        .listRowBackground(colors.surface)
    }

    private var learningSection: some View {
        Section("학습 설정") {
            Toggle(isOn: Binding(get: { dataStore.autoPlay }, set: { dataStore.autoPlay = $0 })) {
                Label("자동 재생 (5초)", systemImage: "play.circle")
                    .foregroundColor(colors.text)
            }
            .tint(colors.accent)

            Toggle(isOn: Binding(get: { dataStore.autoBlur }, set: { dataStore.autoBlur = $0 })) {
                Label("자동 블러 해제 (3초)", systemImage: "eye")
                    .foregroundColor(colors.text)
            }
            .tint(colors.accent)
        }
        .listRowBackground(colors.surface)
    }

    private var notificationSection: some View {
        Section("알림") {
            Toggle(isOn: Binding(
                get: { dataStore.notificationsEnabled },
                set: { v in
                    dataStore.notificationsEnabled = v
                    vm.applyNotification(
                        enabled: v,
                        hour: dataStore.notificationHour,
                        minute: dataStore.notificationMinute
                    )
                }
            )) {
                Label("매일 알림", systemImage: "bell")
                    .foregroundColor(colors.text)
            }
            .tint(colors.accent)

            if dataStore.notificationsEnabled {
                DatePicker(
                    "알림 시간",
                    selection: notificationTimeBinding,
                    displayedComponents: .hourAndMinute
                )
                .foregroundColor(colors.text)
            }
        }
        .listRowBackground(colors.surface)
    }

    private var notificationTimeBinding: Binding<Date> {
        Binding(
            get: {
                var c = DateComponents()
                c.hour   = dataStore.notificationHour
                c.minute = dataStore.notificationMinute
                return Calendar.current.date(from: c) ?? Date()
            },
            set: { date in
                let c = Calendar.current.dateComponents([.hour, .minute], from: date)
                dataStore.notificationHour   = c.hour   ?? 8
                dataStore.notificationMinute = c.minute ?? 0
                vm.applyNotification(
                    enabled: true,
                    hour: dataStore.notificationHour,
                    minute: dataStore.notificationMinute
                )
            }
        )
    }

    private var dataSection: some View {
        Section("데이터") {
            Button {
                Task { await vm.refreshData() }
            } label: {
                HStack {
                    Label("데이터 새로고침", systemImage: "arrow.clockwise")
                        .foregroundColor(colors.text)
                    Spacer()
                    if vm.isRefreshing { ProgressView() }
                }
            }
            if let msg = vm.refreshMessage {
                Text(msg).font(.caption).foregroundColor(colors.textSecondary)
            }
        }
        .listRowBackground(colors.surface)
    }

    private var streakSection: some View {
        Section("학습 기록") {
            HStack {
                Label("연속 접속", systemImage: "flame.fill")
                    .foregroundColor(colors.text)
                Spacer()
                Text("\(dataStore.streakCount)일")
                    .font(.headline.bold())
                    .foregroundColor(colors.accent)
            }
        }
        .listRowBackground(colors.surface)
    }
}
