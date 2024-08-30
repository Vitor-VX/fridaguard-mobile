class ConfigApp {
    constructor() {
        this.packageApp = 'com.endgames.cover.fire.sniper.shooting.game'
    }
}

const GetGlobalContext = () => {
    const context = Java.use('android.app.ActivityThread').currentApplication().getApplicationContext();

    return context;
}

const Toaster = (text) => {
    Java.perform(function () {
        Java.scheduleOnMainThread(() => {
            var toast = Java.use('android.widget.Toast');
            toast.makeText(Java.use('android.app.ActivityThread').currentApplication().getApplicationContext(), Java.use('java.lang.String').$new(text), 1).show();
        });
    });
};

const logApp = (logs) => {
    Java.performNow(() => {
        Java.use('android.util.Log').i('AppFrida', `FRIDA - ${logs.toString()}`);
    });
};

const RequestApp = (token) => {
    const uniqueIdentifier = 'frida-script-' + new Date().getTime();
    let URL = Java.use('java.net.URL');
    let HttpURLConnection = Java.use('java.net.HttpURLConnection');
    let BufferedReader = Java.use('java.io.BufferedReader');
    let InputStreamReader = Java.use('java.io.InputStreamReader');
    let StringBuilder = Java.use('java.lang.StringBuilder');
    let JSONObject = Java.use('org.json.JSONObject');

    try {
        const url = URL.$new('https://fridaguard-js.onrender.com/api/auth/verify-token');
        const connection = Java.cast(url.openConnection(), HttpURLConnection);

        connection.setRequestMethod('POST');
        connection.setDoOutput(true);
        connection.setRequestProperty('Content-Type', 'application/json');
        connection.setRequestProperty('Authorization', 'Bearer ' + token);
        connection.setRequestProperty('X-Frida-Identifier', uniqueIdentifier);

        let responseCode = connection.getResponseCode();
        let inputStreamReader;

        if (responseCode === HttpURLConnection.HTTP_OK.value) {
            inputStreamReader = InputStreamReader.$new(connection.getInputStream(), 'UTF-8');
        } else {
            inputStreamReader = InputStreamReader.$new(connection.getErrorStream(), 'UTF-8');
        }

        let bufferedReader = BufferedReader.$new(inputStreamReader);
        let result = StringBuilder.$new();
        let line;

        while ((line = bufferedReader.readLine()) !== null) {
            result.append(line);
        }

        bufferedReader.close();
        connection.disconnect();

        let jsonResponse = JSONObject.$new(result.toString());
        return jsonResponse.toString();
    } catch (error) {
        logApp('Error request: ' + error.toString());
        return null;
    }
};

const CheckFileModifier = (path) => {
    const File = Java.use('java.io.File');

    const file = File.$new(path);

    if (file.exists() && file.isFile()) {
        return file.length();
    }

    return -1;
}

const ExitProcess = () => {
    const System = Java.use('java.lang.System');
    const Process = Java.use('android.os.Process');
    const myPid = Process.myPid();
    logApp('Exiting process');
    System.exit(0);
    Process.killProcess(myPid);
}

Java.perform(() => {
    const Thread = Java.use('java.lang.Thread');
    const JSONObject = Java.use('org.json.JSONObject');
    const PORT = 6070;
    const config = new ConfigApp();
    const checkFile = CheckFileModifier(`/storage/emulated/0/Android/data/${config.packageApp}/files/script.js`);

    if (checkFile === -1) /* file not exists */ ExitProcess();

    const threadApp = Java.registerClass({
        name: 'com.vx.fridaguard',
        superClass: Thread,
        methods: {
            run: function () {
                try {
                    const ServerSocket = Java.use('java.net.ServerSocket');
                    const BufferedReader = Java.use('java.io.BufferedReader');
                    const InputStreamReader = Java.use('java.io.InputStreamReader');

                    logApp('Server socket criado na porta: ' + PORT);

                    const serverSocket = ServerSocket.$new(PORT);
                    const socket = serverSocket.accept();
                    const input = BufferedReader.$new(InputStreamReader.$new(socket.getInputStream()));
                    let receiverServer = input.readLine();
                    receiverServer = receiverServer.replace('token', '');
                    receiverServer = receiverServer.replace('=', '');

                    if (receiverServer) {
                        let result = RequestApp(receiverServer);
                        const jsonRequest = result !== null ? JSONObject.$new(result) : ExitProcess();

                        if (!jsonRequest.getBoolean('success')) ExitProcess();

                        logApp('App enter');
                    }

                    socket.close();
                    serverSocket.close();
                } catch (e) {
                    logApp('Erro ao tentar iniciar o servidor: ' + e.toString());

                    ExitProcess();
                }
            }
        }
    });

    const thread = threadApp.$new();
    thread.start();
});