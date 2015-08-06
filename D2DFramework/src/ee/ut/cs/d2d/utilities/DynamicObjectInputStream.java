/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package ee.ut.cs.d2d.utilities;

import dalvik.system.DexClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class DynamicObjectInputStream extends ObjectInputStream {

	private ClassLoader cLoader = ClassLoader.getSystemClassLoader();
	private DexClassLoader dLoader = null;

	public DynamicObjectInputStream(InputStream in) 
			throws IOException {
		super(in);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		try {
			try {
				return cLoader.loadClass(desc.getName());
			} catch (ClassNotFoundException e) {
				return dLoader.loadClass(desc.getName());
			}
		} catch (ClassNotFoundException e) {
			return super.resolveClass(desc);
		} catch (NullPointerException e) { 
			return super.resolveClass(desc);
		}

	}

	public void loadAPKClasses(final File apk) {
		if (dLoader == null)
			dLoader = new DexClassLoader(apk.getAbsolutePath(),
					apk.getParentFile().getAbsolutePath(), null, cLoader);
		else
			dLoader = new DexClassLoader(apk.getAbsolutePath(),
					apk.getParentFile().getAbsolutePath(), null, dLoader);
	}

}
