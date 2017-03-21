package no.mofifo.imber.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import no.mofifo.imber.R;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        setTitle(R.string.license_option_title);

        final String rawLicenseText =
                        "date4j License\n" +
                        "\nCopyright (c) 2010-2013 Hirondelle Systems All rights reserved.\n" +
                        "\n" +
                        "Redistribution and use in source and binary forms, with or without" +
                        " modification, are permitted provided that the following conditions are met:\n" +
                        "\n" +
                        "     * Redistributions of source code must retain the above copyright" +
                        " notice, this list of conditions and the following disclaimer.\n" +
                        "     * Redistributions in binary form must reproduce the above copyright" +
                        " notice, this list of conditions and the following disclaimer in the" +
                        "       documentation and/or other materials provided with the distribution.\n" +
                        "     * Neither the name of Hirondelle Systems nor the" +
                        " names of its contributors may be used to endorse or promote products" +
                        " derived from this software without specific prior written permission.\n" +
                        "\n" +
                        " THIS SOFTWARE IS PROVIDED BY HIRONDELLE SYSTEMS ''AS IS'' AND ANY" +
                        " EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED" +
                        " WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE" +
                        " DISCLAIMED. IN NO EVENT SHALL HIRONDELLE SYSTEMS BE LIABLE FOR ANY" +
                        " DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES" +
                        " (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;" +
                        " LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND" +
                        " ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT" +
                        " (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS" +
                        " SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE." +
                        " \n" +
                        "\nSmoothProgressBar License\n" +
                        "\n" +
                        "Copyright 2014 Antoine Merle\n" +
                        "\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\"); " +
                        "you may not use this file except in compliance with the License. " +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software " +
                        "distributed under the License is distributed on an \"AS IS\" BASIS, " +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
                        "See the License for the specific language governing permissions and " +
                        "limitations under the License.\n\n" +
                        "Material Calendar View license\n\n" +
                        "Copyright (c) 2017 Prolific Interactive\n" +
                        "\n" +
                        "Permission is hereby granted, free of charge, to any person obtaining a copy " +
                        "of this software and associated documentation files (the \"Software\"), to deal " +
                        "in the Software without restriction, including without limitation the rights " +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell " +
                        "copies of the Software, and to permit persons to whom the Software is " +
                        "furnished to do so, subject to the following conditions:\n" +
                        "\n" +
                        "The above copyright notice and this permission notice shall be included in " +
                        "all copies or substantial portions of the Software.\n" +
                        "\n" +
                        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR " +
                        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE " +
                        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER " +
                        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, " +
                        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN " +
                        "THE SOFTWARE.";

        TextView licenseTextView = (TextView) findViewById(R.id.licenseTextView);
        licenseTextView.setText(rawLicenseText);
    }
}
