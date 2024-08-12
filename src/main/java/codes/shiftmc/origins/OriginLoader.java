package codes.shiftmc.origins;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public class OriginLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact(
                "com.github.ben-manes.caffeine:caffeine:3.1.8"
        ), null));
        resolver.addDependency(new Dependency(new DefaultArtifact(
                "org.jetbrains.kotlin:kotlin-stdlib:2.0.0"
        ), null));

        resolver.addRepository(new RemoteRepository.Builder(
                "central",
                "default",
                "https://repo1.maven.org/maven2/"
        ).build());

        classpathBuilder.addLibrary(resolver);
    }
}