using System;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Net;
using ICSharpCode.SharpZipLib.Zip;
using ICSharpCode.SharpZipLib.Zip.Compression.Streams;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using System.Xml;

namespace GoodDayLauncher
{
    public partial class MainForm : Form
    {
        private String source_url;
        private String source_file_url;
        private String program_files;
        private String root_directory;
        private String remote_signeture_file;
        private String local_signeture_file;
        private String local_application_directory;
        private String user_name;
        private String password;

        private bool canceled;

        const int SW_HIDE = 0;
        const int SW_SHOWNORMAL = 1;
        const int SW_NORMAL = 1;
        const int SW_SHOWMINIMIZED = 2;
        const int SW_SHOWMAXIMIZED = 3;
        const int SW_MAXIMIZE = 3;
        const int SW_SHOWNOACTIVATE = 4;
        const int SW_SHOW = 5;
        const int SW_MINIMIZE = 6;
        const int SW_SHOWMINNOACTIVE = 7;
        const int SW_SHOWNA = 8;
        const int SW_RESTORE = 9;
        const int SW_SHOWDEFAULT = 10;
        const int SW_FORCEMINIMIZE = 11;
        const int SW_MAX = 11;

        [DllImport("User32.Dll")]
        static extern int ShowWindow(
            IntPtr hWnd,
            int nCmdShow
            );


        public MainForm()
        {
            InitializeComponent();

            progressBar.Maximum = 450000;
            progressBar.Step = 1;

            canceled = false;
      
            source_url = "ftp://opendolphin.good-day.net/dolphin/";
            source_file_url = source_url + "OpenDolphin.zip";
            program_files = Environment.GetFolderPath(Environment.SpecialFolder.ProgramFiles);
            root_directory = program_files + "\\OpenDolphin";
            local_signeture_file = root_directory + "\\version";
            remote_signeture_file = source_url + "version";
            local_application_directory = root_directory + "\\OpenDolphin";
            user_name = "";
            password = "";

            if (isCurrentVersionInstalled(remote_signeture_file, local_signeture_file))
                startButton.Text = "Start";
            else
                startButton.Text = "Download";
        }


        private bool isServaerStarted()
        {
            bool result = true;
            try
            {
                TcpClient tcp = new TcpClient("127.0.0.1", 8009);
                tcp.Close();
            }
            catch (Exception ex)
            {
                result = false;
            }
            return result;
        }

        private void WaitForServerStart()
        {
            while (true)
            {
                try
                {
                    TcpClient tcp = new TcpClient("127.0.0.1", 8009);
                    tcp.Close();
                    break;
                }
                catch (Exception ex)
                { }
            }
            System.Threading.Thread.Sleep(10 * 1000);
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (!startButton.Enabled)
            {
                e.Cancel = (MessageBox.Show("OpenDolphin Installerを終了します。", "", MessageBoxButtons.YesNo) == DialogResult.No);
                canceled = true;
            }
        }

        private void install()
        {
            try
            {
                createDirectory(root_directory);
                decompressFromFtp(source_file_url, local_application_directory);
                if (!canceled)
                    copyFromFtp(remote_signeture_file, local_signeture_file);
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message, "", MessageBoxButtons.OK);
                canceled = true;
                startButton.Enabled = true;
                Close();
            }
        }

        private void download(Uri source, Stream target, String user, String password)
        {
            FtpWebRequest ftpRequest = (FtpWebRequest)WebRequest.Create(source);
            ftpRequest.Method = System.Net.WebRequestMethods.Ftp.DownloadFile;
            ftpRequest.KeepAlive = false;
         //   ftpRequest.Credentials = new System.Net.NetworkCredential(user, password);

            try
            {
                FtpWebResponse ftpResponse = (FtpWebResponse)ftpRequest.GetResponse();
                Stream responseStream = ftpResponse.GetResponseStream();
                try
                {
                    byte[] buffer = new byte[1024];
                    while (true)
                    {
                        int readSize = responseStream.Read(buffer, 0, buffer.Length);
                        if (readSize != 0)
                            target.Write(buffer, 0, readSize);
                        else
                        {
                            int hoge = readSize;
                            break;
                        }

                        if (callback("", "", 1)) return;
                    }
                }
                finally
                {
                    responseStream.Close();
                    ftpResponse.Close();
                }
            }
            catch (Exception e)
            {
                MessageBox.Show("サーバに接続できません。\nネットワーク構成を確認のうえ、再度起動してください。", "ネットワークエラー", MessageBoxButtons.OK);
                throw e;
            }
        }

        private void createDirectory(String path)
        {
            if (!Directory.Exists(path))
              Directory.CreateDirectory(path);
        }

        private void copyFromFtp(String url, String localpath)
        {
            Uri target = new Uri(url);
            String temporary_filename = Path.GetTempFileName();
            Stream file = new FileStream(temporary_filename, System.IO.FileMode.Create, System.IO.FileAccess.Write);
            try
            {
                download(target, file, user_name, password);
                callback(url, localpath, 1);
            }
            finally
            {
                file.Flush();
                file.Close();
            }

            if (File.Exists(localpath))
                File.Delete(localpath);

            if (File.Exists(temporary_filename))
                File.Move(temporary_filename, localpath);
        }

        private void decompressFromFtp(String url, String localpath)
        {
            Uri target = new Uri(url);
            String temporary_filename = Path.GetTempFileName();
            Stream file = new FileStream(temporary_filename, System.IO.FileMode.Create, System.IO.FileAccess.Write);

            try
            {
                download(target, file, user_name, password);
            }
            finally
            {
                file.Flush();
                file.Close();
            }

            if (Directory.Exists(localpath))
            {
                Directory.Delete(localpath, true);
            }
            unZip(temporary_filename);
        }

        private void unZip(String path)
        {
            using (ZipInputStream s = new ZipInputStream(File.OpenRead(path))) 
            {
                ZipEntry theEntry;
                while ((theEntry = s.GetNextEntry()) != null) 
                {
                    string directoryName = Path.GetDirectoryName(theEntry.Name);
                    string fileName = Path.GetFileName(theEntry.Name);
                    
                    // create directory
                    if (directoryName.Length > 0) 
                    {
                        Directory.CreateDirectory(root_directory + "\\" + directoryName);
                    }
                    
                    if (fileName != String.Empty) 
                    {
                        using (FileStream streamWriter = File.Create(root_directory + "\\" + theEntry.Name)) 
                        {
                            int size = 2048;
                            byte[] data = new byte[2048];
                            while (true) 
                            {
                                size = s.Read(data, 0, data.Length);
                                if (size > 0) 
                                {
                                    streamWriter.Write(data, 0, size);
                                } 
                                else 
                                    break;
                                
                               if (callback("", "", 2)) return;
                            }
                        }
                    }
                }
            }
        }

        private Boolean isCurrentVersionInstalled(String url, String local_path)
        {
            if (File.Exists(local_path))
            {
                Uri target = new Uri(url);
                Stream local = new FileStream(local_path, System.IO.FileMode.Open, System.IO.FileAccess.Read);
                Stream remote = new MemoryStream();
                try
                {
                    download(target, remote, user_name, password);
                    remote.Seek(0, 0);
                    StreamReader local_reader = new StreamReader(local, Encoding.GetEncoding("UTF-8"));
                    StreamReader remote_reader = new StreamReader(remote, Encoding.GetEncoding("UTF-8"));
                    String local_version = local_reader.ReadToEnd();
                    String remote_version = remote_reader.ReadToEnd();
                    return (local_version == remote_version);
                }
                finally
                {
                    remote.Close();
                    local.Close();
                }
            }
            return false;
        }

        private Boolean callback(String source, String Target, int increment)
        {
            progressBar.Increment(increment);
            Application.DoEvents();
            return canceled;
        }

        private void startButton_Click(object sender, EventArgs e)
        {
            startButton.Enabled = false;
            if (!isCurrentVersionInstalled(remote_signeture_file, local_signeture_file))
            {
                install();
                startButton.Text = "Start";
                startButton.Enabled = true;
            }
            else
            {
                this.Visible = false;
                startButton.Enabled = true;
                if (!isServaerStarted())
                {
                    System.Diagnostics.Process server = System.Diagnostics.Process.Start(local_application_directory + "\\jboss-4.2.2.GA\\bin\\run.bat", "-b 127.0.0.1");
                    WaitForServerStart();
                    ShowWindow(server.MainWindowHandle, SW_MINIMIZE);
                }
                System.Diagnostics.Process client = System.Diagnostics.Process.Start(local_application_directory + "\\OpenDolphin-Client-WIn.exe");
                Close();
            }
        }

        private String ReadDBSettings()
        {
            String result = "";
            FileStream stream = new FileStream(local_application_directory + "\\jboss-4.2.2.GA\\server\\default\\deploy\\postgres-ds.xml", FileMode.Open);
            XmlReader reader = XmlReader.Create(stream);
            try
            {
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element)
                    {
                        if (reader.Name == "connection-url")
                        {
                            reader.Read();
                            if (reader.NodeType == XmlNodeType.Text)
                            {
                                Uri uri = new Uri(reader.Value);
                                result = uri.Segments[2].Split(':')[0];
                            }
                        }
                    }
                }
            }
            finally
            {
                reader.Close();
                stream.Close();
            }
            return result;
        }

        private void UpdateDBSettings(String address)
        {
            String filepath = local_application_directory + "\\jboss-4.2.2.GA\\server\\default\\deploy\\postgres-ds.xml";
            FileStream read_stream = new FileStream(filepath, FileMode.Open);
            XmlDocument dom = new XmlDocument();
            try
            {
                dom.Load(read_stream);
                XmlNode parent_node = dom.DocumentElement.FirstChild;

                foreach (XmlNode node in parent_node.ChildNodes)
                {
                    if (node.NodeType == XmlNodeType.Element)
                    {
                        if (node.Name == "connection-url")
                        {
                            if (node.FirstChild.NodeType == XmlNodeType.Text)
                            {
                                node.FirstChild.InnerText = "jdbc:postgresql://" + address + ":5432/dolphin";
                                break;
                            }
                        }
                    }
                }
            }
            finally
            {
                read_stream.Close();
            }

            XmlTextWriter writer = new XmlTextWriter(filepath, null);
            try
            {
                dom.WriteContentTo(writer);
            }
            finally
            {
                writer.Close();
            }
        }

        private void データベースToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (File.Exists(local_application_directory + "\\jboss-4.2.2.GA\\server\\default\\deploy\\postgres-ds.xml"))
            {
                try
                {
                    DBAddressDialog dialogForm = new DBAddressDialog();
                    dialogForm.DBAddressTextBox.Text = ReadDBSettings();
                    if (dialogForm.ShowDialog() == DialogResult.OK)
                    {
                        UpdateDBSettings(dialogForm.DBAddressTextBox.Text);
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("postgresqlがインストールされていません。\nインストール後、再度お試しください。", "エラー", MessageBoxButtons.OK);
                }
            }
            else
            {
                MessageBox.Show("postgresqlがインストールされていません。\nインストール後、再度お試しください。", "エラー", MessageBoxButtons.OK);
            }
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
        }

        private void button1_Click(object sender, EventArgs e)
        {

        }


    }

}